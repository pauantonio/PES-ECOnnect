package ECOnnect.UI.Company;

import java.util.Collection;

import javax.swing.*;

import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.HorizontalBox;
import ECOnnect.UI.Utilities.CustomComponents.ItemList;

public class CompanyView extends View {
    private final CompanyController _ctrl;
    private ItemList<CompanyItem> _list;

    // Components

    public CompanyView(CompanyController ctrl){
        this._ctrl = ctrl;
        setUp();
    }

    private void setUp() {
        _list = new ItemList<CompanyItem>(CompanyItem.getHeaderNames(), CompanyItem.getWidths());
        panel.add(_list);

        panel.add(Box.createVerticalStrut(10));
        
        JButton questionsButton = new JButton("Edit questions");
        questionsButton.addActionListener(_ctrl.questionsButton());
        JButton addButton = new JButton("Add new");
        addButton.addActionListener(_ctrl.addButton());
        JButton removeButton = new JButton("Delete selected");
        removeButton.addActionListener(_ctrl.removeButton());
        panel.add(HorizontalBox.create(questionsButton, addButton, removeButton));
        
        panel.add(Box.createVerticalStrut(10));
    }

    void addItem(CompanyItem item){
        _list.add(item);
        _list.redraw();
    }
    
    void addItems(CompanyItem[] items){
        for (CompanyItem item : items) {
            _list.add(item);
        }
        _list.redraw();
    }
    
    Collection<CompanyItem> getSelected(){
        return _list.getSelected();
    }
    
    void deleteItem(CompanyItem item){
        _list.remove(item);
        _list.redraw();
    }
}
