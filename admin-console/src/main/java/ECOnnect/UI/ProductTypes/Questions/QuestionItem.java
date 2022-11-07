package ECOnnect.UI.ProductTypes.Questions;

import java.awt.Component;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import ECOnnect.API.ProductTypesService.ProductType.Question;
import ECOnnect.UI.Utilities.CustomComponents.ItemListElement;

public class QuestionItem extends ItemListElement {

    private final Question _question;
    private final QuestionItemModel _model = new QuestionItemModel();
    
    private final JTextField _statementField = new JTextField();
    private final JButton _editButton = new JButton("Save");
    private final JButton _deleteButton = new JButton("Delete");

    public QuestionItem(Question question) {
        _question = question;
        
        super.init();
    }
    
    public static String[] getHeaderNames() {
        return new String[] {"Statement", "Edit", "Delete"};
    }
    
    public static Integer[] getWidths() {
        return new Integer[] {650, 150, 150};
    }
    
    protected Component[] getRowComponents() {
        _statementField.setText(_question.statement);
        _statementField.setEditable(true);
        _statementField.addCaretListener(statementFieldChangedListener());
        
        _editButton.setEnabled(false);
        _editButton.addActionListener(editButtonListener());
        
        _deleteButton.addActionListener(deleteButtonListener());
                
        return new Component[] {
            _statementField,
            _editButton,
            _deleteButton
        };
    }

    
    private ActionListener editButtonListener() {
        return (ActionEvent e) -> {
            try {
                _model.editQuestion(_question.questionid, _statementField.getText());
                _question.statement = _statementField.getText();
                _editButton.setEnabled(false);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
    
    private ActionListener deleteButtonListener() {
        return (ActionEvent e) -> {
            try {
                _model.deleteQuestion(_question.questionid);
                _editButton.setEnabled(false);
                _deleteButton.setEnabled(false);
                _statementField.setEditable(false);
                _statementField.setText("[DELETED]");
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
    
    private CaretListener statementFieldChangedListener() {
        return (CaretEvent e) -> {
            // If text has changed (and has not been deleted), enable the edit button
            boolean sameText = _statementField.getText().equals(_question.statement);
            if (_statementField.isEditable()) {
                _editButton.setEnabled(!sameText);
            }
        };
    }
    
    @Override
    protected Integer[] getColumnWidths() {
        return getWidths();
    }
    
}
