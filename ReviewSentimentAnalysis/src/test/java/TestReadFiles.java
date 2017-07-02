import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by user on 7/2/2017.
 */
public class TestReadFiles{

    @Test
    public void readSemanticsFile() {
        JSONObject semanticsJsonData = PathUtil.readJsonFile("semantics", "semantics.json");
        assertNotNull(semanticsJsonData);
    }

    @Test
    public void readReviewFiles(){
        List<String> reviews = PathUtil.getFileNames("reviews");
        JSONArray sentiments = new JSONArray();
        for (String reviewFileName:reviews){
            JSONObject sentiment =  PathUtil.readJsonFile("reviews", reviewFileName);
            sentiments.add(sentiment);
        }
        assertEquals(sentiments.size(),5);

    }
}
