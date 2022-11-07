package ECOnnect.UI.Product.ImageDetail;

import ECOnnect.UI.Interfaces.Controller;
import ECOnnect.UI.Interfaces.Screen;
import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.ImageLoader;
import ECOnnect.UI.ScreenManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

public class ImageDetailController extends Controller {
    private final ImageDetailView _view = new ImageDetailView(this);
    private Class<Screen> _previousScreen;

    public View getView() {
        return _view;
    }

    ActionListener backButton() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(_previousScreen);
        };
    }
    
    
    @Override
    public void postInit(Object[] args) {
        // Get arguments
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 arguments: imageUrl:String, previousScreen:Class<Screen>");
        }
        String imageUrl = (String) args[0];
        @SuppressWarnings("unchecked")
        Class<Screen> previousScreen = (Class<Screen>) args[1];
        _previousScreen = previousScreen;
        
        // Callback for image loading
        ImageLoader.loadAsync(imageUrl, new ImageLoader.ImageLoaderCallback() {
            @Override
            public void imageLoaded(ImageIcon image) {
                _view.setIcon(image);
            }
            @Override
            public void couldNotLoad() {
                _view.setText("Could not load URL: " + imageUrl);
            }
        });
    }
}
