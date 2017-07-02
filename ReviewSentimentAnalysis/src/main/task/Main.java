package task;

import edu.cmu.lti.jawjaw.JAWJAW;
import edu.cmu.lti.jawjaw.pobj.POS;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static String REVIEW_PATH = "reviews";
    private static String SEMANTIC_PATH = "semantics";
    private static String SEMANTIC_FILE = "semantics.json";



    public static void main(String[] args) {

        JSONArray sentiments = new JSONArray();

        //reading review files
        List<String> reviews = PathUtil.getFileNames(REVIEW_PATH);
        for (String reviewFileName:reviews){
            JSONObject sentiment =  PathUtil.readJsonFile(REVIEW_PATH, reviewFileName);
            sentiments.add(sentiment);
        }

        //reading semantics
        JSONObject semantics = PathUtil.readJsonFile(SEMANTIC_PATH, SEMANTIC_FILE);

        //preprocessing
        // pp1 splitting review contents into sentences, and sentences into words
        NLPUtils.processAllSentiments(sentiments, semantics);

        String topic;
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a topic: ");
        topic = reader.next().trim();
        List<String> topics = new ArrayList<>();
        for(POS pos:POS.values()){
            topics.addAll(JAWJAW.findSynonyms(topic, pos));
            topics.addAll(JAWJAW.findHypernyms(topic, pos));
        }

        topics.add(topic);


        //now we need to filter sentences based on the topic
        sentiments.stream().map(sentiment->{
                    JSONObject sentimentObj = (JSONObject) sentiment;
                    JSONArray reviewArray = (JSONArray) sentimentObj.get("Reviews");
                    //filtering for topics
                    JSONArray sentencesResult = (JSONArray) reviewArray.stream().map(reviewItem->{
                        JSONArray sentences = (JSONArray)((JSONObject) reviewItem).get("sentences");

                        sentences = (JSONArray) sentences.stream().filter(sentence->{
                            JSONArray words = (JSONArray) ((JSONObject) sentence).get("words");
                            Boolean contains = false;
                            for(int i=0;i<words.size();i++){
                                String lemma = (String)((JSONObject)words.get(i)).get("lemma");
                                if(topics.contains(lemma)){
                                    return true;
                                }
                            }
                            return false;
                        }).collect(Collectors.toCollection(JSONArray::new));
                        return sentences;
                    }).collect(Collectors.toCollection(JSONArray::new));

                    return sentencesResult;
                }).forEach((Object item) ->{
            JSONArray sentimentResult = (JSONArray)item;

            SentimentScore sentimentScore = NLPUtils.getSentimentScore(sentimentResult);
            System.out.print("Positive score:\t"+sentimentScore.getPositiveScore()+"\t\t\tpositive count:\t"+sentimentScore.getPositiveCount());
            System.out.println("\t\t\tNegative score:\t"+sentimentScore.getNegativeScore()+"\t\t\tnegative count:\t"+sentimentScore.getNegativeCount());

        });
    }
}
