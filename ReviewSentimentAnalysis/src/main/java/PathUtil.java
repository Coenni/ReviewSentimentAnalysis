import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/2/2017.
 */
public class PathUtil {

    public static JSONParser parser = new JSONParser();

    public static JSONObject readJsonFile(String path, String fileName) {
        JSONObject resultJson = null;
        InputStream is = PathUtil.class.getResourceAsStream( "/"+path+"/"+fileName);
        try {
            resultJson = (JSONObject) parser.parse(IOUtils.toString( is ));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultJson;

    }

    static List<String> getFileNames(String path) {
        List<String> filenames = new ArrayList<>();
        try(
                InputStream in = getResourceAsStream( path );
                BufferedReader br = new BufferedReader( new InputStreamReader( in ) ) ) {
            String resource;
            while( (resource = br.readLine()) != null ) {
                filenames.add( resource );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filenames;
    }

    private static InputStream getResourceAsStream(String resource) {
        final InputStream in = getContextClassLoader().getResourceAsStream( resource );
        return in == null ? new PathUtil().getClass().getResourceAsStream( resource ) : in;
    }

    private static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
