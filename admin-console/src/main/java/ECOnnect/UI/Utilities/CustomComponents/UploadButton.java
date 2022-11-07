package ECOnnect.UI.Utilities.CustomComponents;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;

import ECOnnect.API.ImageUpload.ImageService;
import ECOnnect.UI.Utilities.ExecutionThread;

public class UploadButton extends ImageButton {

    public static interface IUploadButtonCallback {
        void start();
        void success(String imageUrl);
        void failure(String errorMessage);
    }
    
    private IUploadButtonCallback _callback;
        
    public UploadButton(IUploadButtonCallback callback) {
        // Set the icon
        super("/images/upload.png");
        _callback = callback;
        
        super.addActionListener(e -> clickListener());
    }
    
    public void clickListener() {
        // Open file chooser
        JFileChooser fileChooser = new JFileChooser();
        FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(imageFilter);
        int result = fileChooser.showOpenDialog(null);
        
        // If the user selected a file, get the file path
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        // Disable button while uploading
        this.setEnabled(false);
        _callback.start();
        
        ExecutionThread.nonUI(()->{
            try {
                // Upload image
                File file = fileChooser.getSelectedFile();
                String url = new ImageService().uploadImageToUrl(file);
                
                ExecutionThread.UI(()->{
                    this.setEnabled(true);
                    _callback.success(url);
                });
            }
            catch (Exception e) {
                // If an exception is thrown, the image upload failed
                ExecutionThread.UI(()->{
                    this.setEnabled(true);
                    _callback.failure(e.getMessage());
                });
            }
        });
    }
    
}
