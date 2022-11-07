package ECOnnect.UI.Forum;

import java.awt.event.*;

import ECOnnect.UI.Forum.ForumPostItem.IForumPostCallback;
import ECOnnect.UI.Interfaces.*;
import ECOnnect.UI.Utilities.ExecutionThread;
import ECOnnect.API.ForumService.Post;

public class ForumController extends Controller {

    private final ForumView _view = new ForumView(this);
    private final ForumModel _model = new ForumModel();
    
    
    private void refreshList() {
        _view.setEnabled(false);
        
        ExecutionThread.nonUI(()->{
            // Get posts from model
            try {
                Post[] posts = _model.getPosts();
                ExecutionThread.UI(()->{
                    addItemsToList(posts);
                    _view.clearSearchField();
                    _view.setEnabled(true);
                });
            }
            catch (Exception e) {
                ExecutionThread.UI(()->{
                    _view.displayError("Could not fetch posts: " + e.getMessage());
                });
                return;
            }
        });
    }
    
    private void addItemsToList(Post[] posts) {
        // Convert to forum post items
        ForumPostItem[] postItems = new ForumPostItem[posts.length];
        
        for (int i = 0; i < posts.length; ++i) {
            Post p = posts[i];
            postItems[i] = new ForumPostItem(p, _forumPostCallback);
        }
        
        // Clear the list
        _view.clearList();
        
        // Add the items
        _view.addItems(postItems);
    }
    
    ActionListener searchButton() {
        return (ActionEvent e) -> {
            String name = _view.getSearchName();
            
            // Clear the list
            _view.clearList();
            
            // Get posts from model
            Post[] posts = _model.filterPosts(name);
            
            addItemsToList(posts);
        };
    }
    
    ActionListener refreshButton(){
        return (ActionEvent e) -> {
            // Clear the list
            _view.clearList();
            refreshList();
        };
    }
    
    ActionListener deleteButton(){
        return (ActionEvent e) -> {
            for (ForumPostItem p : _view.getSelected()) {
                try {
                    _model.deletePost(p.getId());
                    _view.removeItem(p);
                }
                catch (Exception ex) {
                    _view.displayError("Could not delete post: " + ex.getMessage());
                }
            }
        };
    }
    
    private IForumPostCallback _forumPostCallback = new IForumPostCallback() {
        @Override
        public boolean banUser(int userId, boolean isBanned) {
            try {
                _model.banUser(userId, isBanned);
                return true;
            }
            catch (Exception e) {
                _view.displayError("Could not ban user: " + e.getMessage());
                return false;
            }
        }
    };
    

    public View getView() {
        return _view;
    }
    
    @Override
    public void postInit(Object[] args) {
        refreshList();
    }
}
