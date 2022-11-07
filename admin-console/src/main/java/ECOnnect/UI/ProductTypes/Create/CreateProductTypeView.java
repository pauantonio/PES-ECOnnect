package ECOnnect.UI.ProductTypes.Create;

import java.awt.*;

import javax.swing.*;

import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.HorizontalBox;

public class CreateProductTypeView extends View {
    private final CreateProductTypeController _ctrl;
    
    private final JTextField _nameTextField = new JTextField(20);
    private final JTextArea _questionsTextArea = new JTextArea();
    
    CreateProductTypeView(CreateProductTypeController ctrl) {
        this._ctrl = ctrl;
        setUp();
    }
    
    
    private void setUp() {
        
        panel.add(Box.createVerticalGlue());
        
        // Set title alignment to center and increase font size
        JLabel title = new JLabel("Create new Product Type");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(24.0f));
        panel.add(HorizontalBox.create(title));
        
        panel.add(Box.createVerticalStrut(50));
        
        JLabel nameLbl = new JLabel("Name of the new type:");
        nameLbl.setPreferredSize(new Dimension(170, 30));
        _nameTextField.setMaximumSize(new Dimension(150, 30));
        panel.add(HorizontalBox.create(nameLbl, _nameTextField));
        // Pressing enter will select the password field
        _nameTextField.addActionListener(e -> _questionsTextArea.requestFocus());
        
        panel.add(Box.createVerticalStrut(10));
        
        JLabel questionsLabel = new JLabel("List of questions (separated by new line):");
        panel.add(HorizontalBox.create(questionsLabel));
        
        JScrollPane scrollPane = new JScrollPane(_questionsTextArea);
        scrollPane.setMaximumSize(new Dimension(500, 300));
        scrollPane.setPreferredSize(new Dimension(500, 300));
        panel.add(scrollPane);
        
        
        panel.add(Box.createVerticalStrut(20));
        
        JButton okButton = new JButton("Save");
        okButton.addActionListener(_ctrl.okButton());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ctrl.cancelButton());
        panel.add(HorizontalBox.create(cancelButton, okButton));
        
        panel.add(Box.createVerticalGlue());
        
    }
    
    String getName() {
        return _nameTextField.getText();
    }
    
    String getQuestions() {
        return _questionsTextArea.getText();
    }
}
