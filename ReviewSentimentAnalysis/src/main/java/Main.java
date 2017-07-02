import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class Main {

    private static String REVIEW_PATH = "reviews";
    private static String SEMANTIC_PATH = "semantics";
    private static String SEMANTIC_FILE = "semantics.json";

    private static JSONArray positiveSemantics;
    private static JSONArray negativeSemantics;
    private static JSONArray intensifierSemantics;
    private static void main(String[] args) {

        JSONArray sentiments = new JSONArray();

        //reading review files
        List<String> reviews = PathUtil.getFileNames(REVIEW_PATH);
        for (String reviewFileName:reviews){
            JSONObject sentiment =  PathUtil.readJsonFile(REVIEW_PATH, reviewFileName);
            sentiments.add(sentiment);
        }

        //reading semantics
        JSONObject semantics = PathUtil.readJsonFile(SEMANTIC_PATH, SEMANTIC_FILE);
        positiveSemantics = (JSONArray)semantics.get("positive");
        negativeSemantics = (JSONArray)semantics.get("negative");
        intensifierSemantics = (JSONArray)semantics.get("intensifier");
    }
}
