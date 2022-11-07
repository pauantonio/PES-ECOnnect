package com.econnect.API.ImageUpload;

import java.io.File;
import java.util.Map;

public interface IUploadClient {
    // Uploads the image to the server and returns the HTTP response
    String upload(String apiUrl, String fileParamName, File file, Map<String, String> extraParams);
}
