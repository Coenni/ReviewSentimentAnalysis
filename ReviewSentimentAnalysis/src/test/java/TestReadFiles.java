import task.PathUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestReadFiles{

    @Test
    public static JSONObject readSemanticsFile() {
        JSONObject semanticsJsonData = PathUtil.readJsonFile("semantics", "semantics.json");
        assertNotNull(semanticsJsonData);
        return semanticsJsonData;
    }

    @Test
    public static JSONArray readReviewFiles(){
        List<String> reviews = PathUtil.getFileNames("reviews");
        JSONArray sentiments = new JSONArray();
        for (String reviewFileName:reviews){
            JSONObject sentiment =  PathUtil.readJsonFile("reviews", reviewFileName);
            sentiments.add(sentiment);
        }
        assertEquals(sentiments.size(),5);
        return sentiments;
    }
}
