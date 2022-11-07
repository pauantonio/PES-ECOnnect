package ECOnnect.UI.Utilities;

import javax.swing.JTextField;

import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.CustomComponents.UploadButton.IUploadButtonCallback;

public class StandardUploadCallback implements IUploadButtonCallback {
    private final View view;
    private final JTextField text;
        
    public StandardUploadCallback(View view, JTextField text) {
        this.view = view;
        this.text = text;
    }
    
    @Override
    public void start() {
        text.setText("Uploading...");
        text.setEditable(false);
    }
    @Override
    public void success(String imageUrl) {
        text.setText(imageUrl);
        text.setEditable(true);
        view.displayMessage("Image uploaded successfully");
    }
    @Override
    public void failure(String errorMessage) {
        text.setText("");
        text.setEditable(true);
        view.displayWarning("Upload failed:\n" + errorMessage);
    }
}