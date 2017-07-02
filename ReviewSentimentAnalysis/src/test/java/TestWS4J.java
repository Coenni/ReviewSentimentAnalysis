import task.WordnetSimilarityUtil;
import org.junit.Assert;
import org.junit.Test;

public class TestWS4J {

    @Test
    public void similarityTest() {
        long t0 = System.currentTimeMillis();
        String firstWord = "staff";
        String secondWord = "personnel";
        double similarityIndex = WordnetSimilarityUtil.compute( "staff","personnel" );
        Assert.assertTrue(similarityIndex>0.85);
    }


}
