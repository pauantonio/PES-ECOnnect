package ECOnnect.UI.ProductTypes;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import ECOnnect.API.ProductTypesService.ProductType;
import ECOnnect.UI.Utilities.CustomComponents.ItemListElement;

public class ProductTypeItem extends ItemListElement {
    
    public interface IProductTypeItemCallback {
        void viewProducts(int index);
        void viewQuestions(int index);
        boolean renameType(int index, String newName);
    }
    
    private final int _index;
    private final ProductType _productType;
    private final JCheckBox _deleteCheckBox = new JCheckBox();
    private final JTextField _nameField = new JTextField();
    private final JButton _editButton = new JButton("Rename");
    private final IProductTypeItemCallback _owner;

    public ProductTypeItem(IProductTypeItemCallback owner, int index, ProductType productType) {
        this._owner = owner;
        this._index = index;
        this._productType = productType;
        
        super.init();
    }
    
    @Override
    public String getName() {
        return _productType.name;
    }
    
    public static String[] getHeaderNames() {
        return new String[] {"Name", "Rename", "# Questions", "See questions", "View all products", "Select for delete"};
    }
    
    public static Integer[] getWidths() {
        return new Integer[] {200, 100, 120, 150, 150, 150};
    }
    
    protected Component[] getRowComponents() {
        
        _nameField.setText(_productType.name);
        _nameField.setEditable(true);
        _nameField.addCaretListener(statementFieldChangedListener());
        
        _editButton.setEnabled(false);
        _editButton.addActionListener(editButtonListener());
        
        JTextField numQuestionsField = new JTextField(Integer.toString(_productType.questions.length));
        numQuestionsField.setEditable(false);
        
        JButton seeProductsButton = new JButton("Products");
        seeProductsButton.addActionListener(seeProductsButtonListener());
        
        JButton seeQuestionsButton = new JButton("Questions");
        seeQuestionsButton.addActionListener(seeQuestionsButtonListener());
                
        return new Component[] {
            _nameField,
            _editButton,
            numQuestionsField,
            seeQuestionsButton,
            seeProductsButton,
            _deleteCheckBox
        };
    }

    
    private ActionListener seeProductsButtonListener() {
        return (ActionEvent e) -> {
            _owner.viewProducts(_index);
        };
    }
    
    private ActionListener seeQuestionsButtonListener() {
        return (ActionEvent e) -> {
            _owner.viewQuestions(_index);
        };
    }
    
    private ActionListener editButtonListener() {
        return (ActionEvent e) -> {
            boolean success = _owner.renameType(_index, _nameField.getText());
            if (success) {
                _productType.name = _nameField.getText();
                _nameField.setText(_productType.name);
                _editButton.setEnabled(false);
                super.redraw();
            }
        };
    }
    
    private CaretListener statementFieldChangedListener() {
        return (CaretEvent e) -> {
            // If text has changed (and has not been deleted), enable the edit button
            boolean sameText = _nameField.getText().equals(_productType.name);
            if (_nameField.isEditable()) {
                _editButton.setEnabled(!sameText);
            }
        };
    }
    
    
    @Override
    public boolean isSelected() {
        return _deleteCheckBox.isSelected();
    }
    
    @Override
    public void uncheck() {
        _deleteCheckBox.setSelected(false);
    }

    @Override
    protected Integer[] getColumnWidths() {
        return getWidths();
    }
}
