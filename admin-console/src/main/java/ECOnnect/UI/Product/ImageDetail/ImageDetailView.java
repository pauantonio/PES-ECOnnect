package ECOnnect.UI.Product.ImageDetail;

import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.HorizontalBox;

import javax.swing.*;

public class ImageDetailView extends View {
    private final ImageDetailController _ctrl;
    private final JLabel _thumbnail = new JLabel();

    // Coponents

    public ImageDetailView(ImageDetailController ctrl){
        this._ctrl = ctrl;
        setUp();
    }

    private void setUp() {
        _thumbnail.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane scrollPane = new JScrollPane(_thumbnail);
        panel.add(scrollPane);
        
        panel.add(Box.createVerticalStrut(20));
        
        // Back button
        JButton cancelButton = new JButton("Back");
        cancelButton.addActionListener(_ctrl.backButton());
        panel.add(HorizontalBox.create(cancelButton));
        
        panel.add(Box.createVerticalStrut(20));
    }
    
    void setIcon(ImageIcon image){
        _thumbnail.setIcon(image);
    }
    
    void setText(String text){
        _thumbnail.setText(text);
    }

}
