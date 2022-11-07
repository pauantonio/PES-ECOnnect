package ECOnnect.API;

import java.util.TreeMap;

import ECOnnect.API.Exceptions.ApiException;

public class ForumService extends Service {
    // Only allow instantiating from ServiceFactory
    ForumService() {}
    
    public static class Post {
        // Important: The name of these attributes must match the ones in the returned JSON
        public final int postid;
        public final String username;
        public final int userid;
        public final String medal;
        public final String text;
        public final String imageurl;
        public final int likes;
        public final int dislikes;
        public final int useroption;
        public final float timestamp;
        public final boolean ownpost;
        public final int timesreported;
        public boolean authorbanned;
        
        public Post(int postId, String username, int userId, String medal, String text, String imageURL, int likes, int dislikes, int userOption, float timestamp, boolean ownPost, int timesReported, boolean authorBanned) {
            this.postid = postId;
            this.username = username;
            this.userid = userId;
            this.medal = medal;
            this.text = text;
            this.imageurl = imageURL;
            this.likes = likes;
            this.dislikes = dislikes;
            this.useroption = userOption;
            this.timestamp = timestamp;
            this.ownpost = ownPost;
            this.timesreported = timesReported;
            this.authorbanned = authorBanned;
        }
    }
    
    // Get all posts
    public Post[] getPosts(int numPosts, String tag) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.POST_AMOUNT, Integer.toString(numPosts));
        // No tag means all posts
        if (tag != null) params.put(ApiConstants.POST_TAG, tag);
        
        JsonResult result = null;
        try {
            // Call API
            super.needsToken = true;
            result = get(ApiConstants.POSTS_PATH, params);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                // This endpoint does not throw any errors
                default:
                    throw e;
            }
        }
        
        // Parse result
        Post[] posts = result.getArray(ApiConstants.RET_RESULT, Post[].class);
        assertResultNotNull(posts, result);
        return posts;
    }
    
    // Delete a post
    public void deletePost(int postId) {
        
        JsonResult result = null;
        try {
            // Call API
            super.needsToken = true;
            result = delete(ApiConstants.POSTS_PATH + "/" + postId, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_POST_NOT_EXISTS:
                    throw new RuntimeException("The post with id=" + postId + " does not exist");
                default:
                    throw e;
            }
        }
        expectOkStatus(result);
    }
    
    
    // Check if a user is banned
    public boolean isBanned(int userId) {
        JsonResult result = null;
        try {
            // Call API
            super.needsToken = true;
            result = get(String.format(ApiConstants.BAN_PATH, userId), null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_USER_NOT_EXISTS:
                    throw new RuntimeException("The user with id " + userId + " does not exist");
                default:
                    throw e;
            }
        }
        
        // Parse result
        String isBanned = result.getAttribute(ApiConstants.RET_RESULT);
        assertResultNotNull(isBanned, result);
        return isBanned.equalsIgnoreCase("true");
    }
    
    // Ban a user
    public void banUser(int userId, boolean isBanned) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.BAN_SET_BANNED, Boolean.toString(isBanned));
        
        JsonResult result = null;
        try {
            // Call API
            super.needsToken = true;
            result = post(String.format(ApiConstants.BAN_PATH, userId), params, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_USER_NOT_EXISTS:
                    throw new RuntimeException("The user with id " + userId + " does not exist");
                case ApiConstants.ERROR_CANNOT_BAN_YOURSELF:
                    throw new RuntimeException("You cannot ban yourself");
                default:
                    throw e;
            }
        }
        expectOkStatus(result);
    }
}
