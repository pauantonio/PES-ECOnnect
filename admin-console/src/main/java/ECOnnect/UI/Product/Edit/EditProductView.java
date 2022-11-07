package ECOnnect.UI.Product.Edit;

import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.HorizontalBox;
import ECOnnect.UI.Utilities.StandardUploadCallback;
import ECOnnect.UI.Utilities.CustomComponents.UploadButton;

import javax.swing.*;
import java.awt.*;

public class EditProductView extends View {
    private final IEditProductController _ctrl;

    private final JTextField _nameTxt = new JTextField(26);
    private final JTextField _manufacturerTxt = new JTextField(26);
    private final JTextField _imageUrlTxt = new JTextField(20);
    private final JLabel _title = new JLabel("", SwingConstants.CENTER);
    
    private final Dimension _dim = new Dimension(110, 30);

    public EditProductView(IEditProductController ctrl){
        this._ctrl = ctrl;
        setUp();
    }

    private void setUp() {
        panel.add(Box.createVerticalGlue());
        
        // Increase title font size
        _title.setFont(_title.getFont().deriveFont(24.0f));
        panel.add(HorizontalBox.create(_title));
        
        panel.add(Box.createVerticalStrut(50));

        JLabel nameLbl = new JLabel("Product name:", SwingConstants.RIGHT);
        nameLbl.setPreferredSize(_dim);
        _nameTxt.setMaximumSize(_dim);
        panel.add(HorizontalBox.create(nameLbl, _nameTxt));
        // Pressing enter will select the next text field
        _nameTxt.addActionListener(e -> _manufacturerTxt.requestFocus());

        panel.add(Box.createVerticalStrut(10));

        JLabel manufacturerLbl = new JLabel("Manufacturer:", SwingConstants.RIGHT);
        manufacturerLbl.setPreferredSize(_dim);
        _manufacturerTxt.setMaximumSize(_dim);
        panel.add(HorizontalBox.create(manufacturerLbl, _manufacturerTxt));
        // Pressing enter will select the next text field
        _manufacturerTxt.addActionListener(e -> _imageUrlTxt.requestFocus());

        panel.add(Box.createVerticalStrut(10));

        JLabel imageUrlLbl = new JLabel("Image URL:", SwingConstants.RIGHT);
        JButton uploadButton = new UploadButton(new StandardUploadCallback(this, _imageUrlTxt));
        imageUrlLbl.setPreferredSize(_dim);
        _imageUrlTxt.setMaximumSize(_dim);
        panel.add(HorizontalBox.create(imageUrlLbl, uploadButton, _imageUrlTxt));

        panel.add(Box.createVerticalStrut(10));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ctrl.saveButton());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ctrl.cancelButton());
        panel.add(HorizontalBox.create(cancelButton, saveButton));

        panel.add(Box.createVerticalGlue());
    }
    
    public void setTitle(String text) {
        _title.setText(text);
    }
    
    public void setFields(String name, String manufacturer, String imageUrl) {
        _nameTxt.setText(name);
        _manufacturerTxt.setText(manufacturer);
        _imageUrlTxt.setText(imageUrl);
    }

    public String getNameText(){return _nameTxt.getText();}
    public String getManufacturerText(){return _manufacturerTxt.getText();}
    public String getImageUrlText(){return _imageUrlTxt.getText();}

}
