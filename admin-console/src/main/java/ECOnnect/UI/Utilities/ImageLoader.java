package ECOnnect.UI.Utilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class ImageLoader {
    // Cannot instantiate utility class
    private ImageLoader() {}
    
    public static interface ImageLoaderCallback {
        public void imageLoaded(ImageIcon image);
        public void couldNotLoad();
    }
    
    public static void loadAsync(String url, ImageLoaderCallback callback) {
        ExecutionThread.nonUI(() -> {
            BufferedImage result = null;
            try {
                result = ImageIO.read(new URL(url));
            }
            catch (IOException e) {
                // Could not load image, keep result null
            }
            
            if (result == null) {
                callback.couldNotLoad();
            }
            else {
                final ImageIcon icon = new ImageIcon(result);
                ExecutionThread.UI(() -> callback.imageLoaded(icon));
            }
        });
    }
    
    public static ImageIcon scale(ImageIcon icon, int width, int heigth) {
        Image image = icon.getImage().getScaledInstance(width, heigth, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
