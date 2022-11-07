package com.econnect.API.ImageUpload;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ApacheUploadClient implements IUploadClient {
    
    private static CloseableHttpClient _httpclient = null;
    
    public ApacheUploadClient() {
        if (_httpclient == null) {
            _httpclient = HttpClients.createDefault();
        }
    }

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
