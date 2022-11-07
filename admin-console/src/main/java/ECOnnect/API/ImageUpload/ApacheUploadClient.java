package ECOnnect.API.ImageUpload;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ApacheUploadClient implements IUploadClient {
    
    private static final CloseableHttpClient _httpclient = HttpClients.createDefault();

    @Override
    public String upload(String apiUrl, String fileParamName, File file, Map<String, String> extraParams) {
        
        // Build request
        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // Add file
        ContentBody cb = new FileBody(file);
        builder.addPart(fileParamName, cb);
        // Add extra params
        for (Map.Entry<String, String> entry : extraParams.entrySet()) { 
            builder.addTextBody(entry.getKey(), entry.getValue());
        }
        
        final HttpPost httppost = new HttpPost(apiUrl);
        httppost.setEntity(builder.build());
        
        
        // Execute request
        String postResult = null;
        try {
            HttpResponse response = _httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            postResult = EntityUtils.toString(resEntity, "UTF-8");
        }
        catch (IOException e) {
            throw new RuntimeException("Error uploading image: " + e.getMessage(), e);
        }
        return postResult;
    }
}
