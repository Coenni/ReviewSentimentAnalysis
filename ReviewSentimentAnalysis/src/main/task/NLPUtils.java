package task;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.util.CoreMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

class NLPUtils {

    private static StanfordCoreNLP pipeline = new StanfordCoreNLP(new Properties(){{
        setProperty("annotators", "tokenize,ssplit,pos,lemma");
    }});

    private static JSONArray processSentimentContent(String content, JSONObject semantics){
        JSONArray sentences = new JSONArray();
        DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(content));
        dp.iterator().forEachRemaining(sentence -> {
            JSONArray wordsInSentence = new JSONArray();
            for (HasWord hasWords:  sentence) {
                wordsInSentence.add(produceWordData(hasWords, semantics));
            }

            List<Score> score = calculateScore(wordsInSentence);
            //making efficient time&space using
            if(score.size()>0){
                JSONObject sentenceData  = new JSONObject();
                sentenceData.put("words", wordsInSentence);
                String sentenceStr = SentenceUtils.listToString(sentence);
                sentenceData.put("sentence", sentenceStr);
                sentenceData.put("score", score);
                sentences.add(sentenceData);
            }
        });
        return sentences; //we have only useful sentences for sentiment analysis
    }




    static void processAllSentiments(JSONArray sentiments, JSONObject semantics) {
        sentiments = (JSONArray) sentiments.parallelStream()
                .map(jsonObject->processUnitSentiment((JSONArray)(((JSONObject)jsonObject).get("Reviews")), semantics))
                .collect(Collectors.toCollection(JSONArray::new));
    }

    private static JSONArray processUnitSentiment(JSONArray reviews, JSONObject semantics){
        Object reviewsProcessed =  reviews.parallelStream().map(item->{
            JSONObject jsonObject = (JSONObject)item;
            jsonObject.put("sentences",processSentimentContent(((String)jsonObject.get("Content")), semantics));
            return jsonObject;
        }).collect(Collectors.toCollection(JSONArray::new))
                ;
        return (JSONArray)reviewsProcessed;
    }

    private static List<Score> calculateScore(JSONArray wordsInSentence) {
        List<Score> scores = new ArrayList<>();
        for (int i=0;i<wordsInSentence.size();i++){
            JSONObject jo = (JSONObject) wordsInSentence.get(i);
            Double intens = 1.0;
            Score score = null;
            if(jo.get("tag")!=null){
                Double val = Double.parseDouble(jo.get("tagValue").toString());
                if(jo.get("tag").equals("pos")){
                    score = new Score();
                    score.setVal(val);
                    score.setM(intens);
                    scores.add(score);
                    intens = 1.0;
                }
                if(jo.get("tag").equals("neg")){
                    score = new Score();
                    score.setVal(-1*val);
                    score.setM(intens);
                    scores.add(score);
                    intens = -1.0;
                }
                if(jo.get("tag").equals("intens")){
                    intens = val;
                }
            }
        }
        return scores;
    }

    private static JSONObject produceWordData(HasWord hasWords, JSONObject semantics) {
        JSONObject wordData = new JSONObject();
        String wordText = hasWords.word();
        wordData.put("value",wordText);
        String lemma = getLemma(wordText);
        wordData.put("lemma",lemma);

        JSONArray positiveSemantics = (JSONArray) semantics.get("positive");
        JSONArray negativeSemantics = (JSONArray) semantics.get("negative");
        JSONArray intensifierSemantics = (JSONArray) semantics.get("intensifier");
        //adding ners
        positiveSemantics.parallelStream().filter(item->{
            JSONObject jsonItem = (JSONObject)item;
            return ((String)jsonItem.get("phrase")).equals(lemma.toLowerCase());
        }).forEach(semantic -> {
            wordData.put("tag","pos");
            wordData.put("tagValue", ((JSONObject)semantic).get("value"));
        });

        negativeSemantics.parallelStream().filter(item->{
            JSONObject jsonItem = (JSONObject)item;
            return ((String)jsonItem.get("phrase")).equals(lemma.toLowerCase());
        }).forEach(semantic -> {
            wordData.put("tag","neg");
            wordData.put("tagValue", ((JSONObject)semantic).get("value"));
        });

        //
        intensifierSemantics.parallelStream().filter(item->{
            JSONObject jsonItem = (JSONObject)item;
            return ((String)jsonItem.get("phrase")).equals(lemma.toLowerCase());
        }).forEach(semantic -> {
            wordData.put("tag","intens");
            wordData.put("tagValue", ((JSONObject)semantic).get("multiplier"));
        });


        return wordData;
    }

    private static String getLemma(String wordText) {
        Annotation tokenAnnotation = new Annotation(wordText);
        pipeline.annotate(tokenAnnotation);  // necessary for the LemmaAnnotation to be set.
        List<CoreMap> list = tokenAnnotation.get(CoreAnnotations.SentencesAnnotation.class);
        String tokenLemma = list
                .get(0).get(CoreAnnotations.TokensAnnotation.class)
                .get(0).get(CoreAnnotations.LemmaAnnotation.class);
        return tokenLemma;
    }

    static SentimentScore getSentimentScore(JSONArray sentimentResult) {
        SentimentScore sentimentScore = new SentimentScore();
        for(int i=0;i<sentimentResult.size();i++){
            JSONArray ja = (JSONArray) sentimentResult.get(i);
            for(int j=0;j<ja.size();j++){
                ArrayList<Score> scoreList = (ArrayList<Score>) ((JSONObject)ja.get(j)).get("score");
                for (Score score: scoreList
                        ) {
                    Double scoreValue = score.getM()* score.getVal();
                    if(scoreValue>0){
                        sentimentScore.incrementPositiveScore(scoreValue);
                        sentimentScore.incrementPositiveCount();
                    }
                    if(scoreValue<0){
                        sentimentScore.incrementNegativeScore(scoreValue);
                        sentimentScore.incrementNegativeCount();
//                        negativeScore+=scoreValue;
//                        negativeCount++;
                    }
                }
            }

        }
        return sentimentScore;
    }
}
