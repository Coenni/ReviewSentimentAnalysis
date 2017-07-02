# ReviewSentimentAnalysis

Project is about sentiment analysis of hotel reviews.
<br/>
In this task firstly, the reviews are preprocessed, tokenized, lemmatized, splitted into sentences
and tagger as NER with semantics dictionary with type(pos/neg), value and multiplier.
The sentences without semantic tag are filtered out to make traverse efficient in future works.
To improve topics control lemma information is used.
Then based on user-entered topic, synonyms and hypernyms words(based on Wordnet) are generated.
<br/>
Then as structural each reviews content is accepted as list of sentences. And topic based informations
are scored based on sentences which includes topics.
<br/>
There are some weak points in current state:
1. sarcasm like general NLP problems
2. semantics dictionary elements which includes more than one word.
3. Room was bad, breakfast was good.-sentence like this will be scored same and incorrect, because
it calculates general score based on topic-contained sentence.
...
Briefly, it is simplified task and needs to be improved.

To test the application, run the ReviewSentimentAnalysis/src/main/task/Main.java.