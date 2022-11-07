package ECOnnect.UI.Forum;

import java.util.ArrayList;

import ECOnnect.API.ForumService;
import ECOnnect.API.ServiceFactory;
import ECOnnect.API.ForumService.Post;

public class ForumModel {
    
    private Post[] _posts;
    
    // Get all the posts from the forum
    Post[] getPosts() {
        // Get products from API
        ForumService service = ServiceFactory.getInstance().getForumService();
        // Get the latest 1000 posts with any tag
        Post[] p = service.getPosts(1000, null);
        
        // Store in model
        _posts = p;
        
        return p;
    }
    
    // Get posts filtered by a user name
    Post[] filterPosts(String name) {
        name = name.trim().toLowerCase();
        ArrayList<Post> filteredPosts = new ArrayList<>();
        for (Post p : _posts) {
            if (p.username.toLowerCase().contains(name)) {
                filteredPosts.add(p);
            }
        }
        return filteredPosts.toArray(new Post[filteredPosts.size()]);
    }
    
    // Delete a post
    void deletePost(int postid) {
        ForumService service = ServiceFactory.getInstance().getForumService();
        service.deletePost(postid);
    }

    // Ban or unban a user
    public void banUser(int userId, boolean isBanned) {
        ForumService service = ServiceFactory.getInstance().getForumService();
        service.banUser(userId, isBanned);
    }
}
