package ECOnnect.UI.ProductTypes.Questions;

import java.awt.event.*;

import javax.swing.*;

import ECOnnect.API.ProductTypesService.ProductType.Question;
import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.HorizontalBox;
import ECOnnect.UI.Utilities.CustomComponents.ItemList;

public class QuestionsView extends View {
    
    public interface INewQuestionCallback {
        void onNewQuestion();
    }
    
    private final JLabel _title = new JLabel("", SwingConstants.CENTER);
    private final INewQuestionCallback _callback;
    private ItemList<QuestionItem> _list;
    
    public QuestionsView(INewQuestionCallback callback) {
        _callback = callback;
        setUp();
    }
    
    
    private void setUp() {
        
        _list = new ItemList<>(QuestionItem.getHeaderNames(), QuestionItem.getWidths());
        
        panel.add(Box.createVerticalStrut(20));
        
        // Increase title font size
        _title.setFont(_title.getFont().deriveFont(24.0f));
        panel.add(HorizontalBox.create(_title));
        
        panel.add(Box.createVerticalStrut(20));
        
        panel.add(_list);
        
        panel.add(Box.createVerticalStrut(20));
        
        JButton backButton = new JButton("Go back");
        backButton.addActionListener((ActionEvent e) -> {
            ScreenManager.getInstance().show(ScreenManager.MAIN_MENU_SCREEN);
        });
        JButton addButton = new JButton("Add new");
        addButton.addActionListener((ActionEvent e) -> {
            _callback.onNewQuestion();
        });
        panel.add(HorizontalBox.create(backButton, addButton));
        
        panel.add(Box.createVerticalStrut(20));
    }
    
    public void setQuestions(Question[] questions) {
        _list.clear();
        for (Question question : questions) {
            _list.add(new QuestionItem(question));
        }
        _list.redraw();
    }
    
    public void setTitle(String title) {
        ScreenManager.getInstance().updateTitle(title);
        _title.setText(title);
    }
}
