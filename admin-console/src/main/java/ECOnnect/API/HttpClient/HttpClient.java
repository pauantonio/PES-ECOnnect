package ECOnnect.API.HttpClient;

import java.io.IOException;
import java.util.Map;

public interface HttpClient {
    // GET a URL and return the response body as a string. params may be null
    String get(String url, Map<String,String> params) throws IOException;
    
    // POST JSON to URL and return the response body as a string. params may be null
    String post(String url, Map<String,String> params, String json) throws IOException;
    
    // DELETE request on a URL and return the response body as a string. params may be null
    String delete(String url, Map<String,String> params) throws IOException;
    
    // PUT JSON to URL and return the response body as a string. params may be null
    String put(String url, Map<String,String> params, String json) throws IOException;
}
