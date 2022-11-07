package ECOnnect.UI.Forum;

import ECOnnect.API.ForumService.Post;
import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Product.ImageDetail.ImageDetailScreen;
import ECOnnect.UI.Utilities.ImageLoader;
import ECOnnect.UI.Utilities.CustomComponents.ItemListElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ForumPostItem extends ItemListElement {
    
    public static interface IForumPostCallback {
        public boolean banUser (int userId, boolean isBanned);
    }
    
    private final Post _post;
    private final IForumPostCallback _callback;
    private final JCheckBox _deleteCheckbox = new JCheckBox();
    private final JButton _banButton = new JButton();

    public ForumPostItem(Post post, IForumPostCallback callback) {
        _post = post;
        _callback = callback;
        
        super.init();
    }
    
    public int getId() {
        return _post.postid;
    }

    public static String[] getHeaderNames(){
        return new String[] {"Author", "Text", "Thumbnail", "Full image", "Likes/Disl.", "Reports", "Ban author", "Select for delete"};
    }
    
    public static Integer[] getWidths(){
        return new Integer[] {150, 350, 100, 120, 100, 100, 100, 150};
    }

    protected Component[] getRowComponents(){
        JTextField authorField = new JTextField(_post.username);
        authorField.setEditable(false);
        
        JTextField textField = new JTextField(_post.text.replace("\n", " "));
        textField.setEditable(false);
        
        JLabel thumbnail = new JLabel();
        thumbnail.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton imageButton = new JButton("View");
        imageButton.addActionListener(imageButtonListener());
        
        JTextField likeDislikeField = new JTextField(_post.likes + " / " + _post.dislikes);
        likeDislikeField.setEditable(false);
        
        JTextField reportField = new JTextField(Integer.toString(_post.timesreported));
        reportField.setEditable(false);
        
        _banButton.setText(_post.authorbanned ? "Unban" : "Ban");
        _banButton.addActionListener(banButtonListener());
        
        // Callback for image loading
        ImageLoader.loadAsync(_post.imageurl, new ImageLoader.ImageLoaderCallback() {
            @Override
            public void imageLoaded(ImageIcon image) {
                ImageIcon scaledImage = ImageLoader.scale(image, -1, DEFAULT_SIZE.height);
                thumbnail.setIcon(scaledImage);
                redraw();
            }
            @Override
            public void couldNotLoad() {
                thumbnail.setText(_post.imageurl);
            }
        });
        
        return new Component[] {
            authorField,
            textField,
            thumbnail,
            imageButton,
            likeDislikeField,
            reportField,
            _banButton,
            _deleteCheckbox
        };
    }
    
    private ActionListener imageButtonListener() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(ImageDetailScreen.class, _post.imageurl, ScreenManager.MAIN_MENU_SCREEN);
        };
    }
    
    private ActionListener banButtonListener() {
        return (ActionEvent e) -> {
            boolean success = _callback.banUser(_post.userid, !_post.authorbanned);
            if (success) {
                _post.authorbanned = !_post.authorbanned;
                _banButton.setText(_post.authorbanned ? "Unban" : "Ban");
            }
        };
    }

    @Override
    public boolean isSelected() {
        return _deleteCheckbox.isSelected();
    }

    @Override
    public void uncheck() {
        _deleteCheckbox.setSelected(false);
    }
    
    @Override
    protected Integer[] getColumnWidths() {
        return getWidths();
    }
}
