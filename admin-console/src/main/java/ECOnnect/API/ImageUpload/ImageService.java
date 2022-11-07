package ECOnnect.API.ImageUpload;

import java.io.File;
import java.util.TreeMap;

import ECOnnect.API.JsonResult;


public class ImageService {
    
    // https://docs.google.com/document/d/16M3qaw27vgwuwXqExo0aIC0nni42OOuWu_OGvpYl7dE/pub
    
    public static class UploadedImage {
        public final String id; // Unique alphanumeric image id
        public final int server; // Server location
        public final int bucket; // bucket directory
        public final String filename; // The filename on the server
        public final String original_filename; // The original filename sent from user
        public final String direct_link; // Direct link to the file
        public final String title; // User generated image title
        public final String album_id; // Unique alphanumeric album id. Only displays if image is in an album
        public final String album_title; // Album tile. Only displays if image is in an album
        public final int creation_date; // The creation date/time as a unix timestamp
        public final boolean hidden; // States whether the image has been deleted
        public final int filesize; // file size in kilobytes
        public final int width; // The width of the image
        public final int height; // the height of the image
        
        public UploadedImage (String id, int server, int bucket, String filename, String originalFilename, String directLink, String title, String albumId, String albumTitle, int creationDate, boolean hidden, int filesize, int width, int height) {
            this.id = id;
            this.server = server;
            this.bucket = bucket;
            this.filename = filename;
            this.original_filename = originalFilename;
            this.direct_link = directLink;
            this.title = title;
            this.album_id = albumId;
            this.album_title = albumTitle;
            this.creation_date = creationDate;
            this.hidden = hidden;
            this.filesize = filesize;
            this.width = width;
            this.height = height;
        }
    }
    
    public static class UploadResult {
        public final int max_filesize; // The maximum filesize the current user is allowed in kilobytes
        public final int space_limit; // The maximum account storage limit in kilobytes
        public final int space_used; // The current storage used in kilobytes
        public final int space_left; // The difference from space_limit and space_used in kilobytes
        public final int passed; // The number of images uploaded successfully
        public final int failed; // The number of images upload failures
        public final UploadedImage[] images; // An array of the Basic Image Models successfully uploaded
        
        public UploadResult(int maxFilesize, int spaceLimit, int spaceUsed, int spaceLeft, int passed, int failed, UploadedImage[] images) {
            this.max_filesize = maxFilesize;
            this.space_limit = spaceLimit;
            this.space_used = spaceUsed;
            this.space_left = spaceLeft;
            this.passed = passed;
            this.failed = failed;
            this.images = images;
        }
    }
    
    private static final String _IMAGESHACK_UPLOAD_URL = "https://api.imageshack.com/v2/images";
    
    private final IUploadClient _uploadClient = new ApacheUploadClient();
    
    // Upload an image, returns an UploadResult object
    public UploadResult uploadImage(File file) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put("key", ApiKey.IMAGESHACK_TOKEN);
        params.put("album", "my_album");
        params.put("comments_disabled", "true");
        
        // Parse result
        String resultString = _uploadClient.upload(_IMAGESHACK_UPLOAD_URL, "file", file, params);
        JsonResult result = JsonResult.parse(resultString);
        
        Boolean success = result.getObject("success", Boolean.class);
        if (success == null || !success) {
            throw new RuntimeException("Image upload failed");
        }
                
        return result.getObject("result", UploadResult.class);
    }
    
    // Upload an image to a server and return the url to the uploaded image
    public String uploadImageToUrl(File file) {
        UploadResult uploadResult = uploadImage(file);
        return "https://" + uploadResult.images[0].direct_link;
    }
    
}
