package ECOnnect.UI.Forum;

import java.util.ArrayList;

import javax.swing.*;

import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.HorizontalBox;
import ECOnnect.UI.Utilities.CustomComponents.ItemList;
import ECOnnect.UI.Utilities.CustomComponents.ItemListElement;

public class ForumView extends View {
    private final ForumController _ctrl;
    private ItemList<ForumPostItem> _list;
    
    private final JButton _searchButton = new JButton("Search");
    private final JButton _refreshButton = new JButton("Refresh");
    private final JButton _deleteButton = new JButton("Delete selected");
    private final JTextField _searchField = new JTextField();

    // Components

    public ForumView(ForumController ctrl){
        this._ctrl = ctrl;
        setUp();
    }

    private void setUp() {
        _list = new ItemList<ForumPostItem>(ForumPostItem.getHeaderNames(), ForumPostItem.getWidths());
        panel.add(_list);

        panel.add(Box.createVerticalStrut(10));
        
        JLabel searchLabel = new JLabel("Search by name:");
        _searchButton.addActionListener(_ctrl.searchButton());
        _searchField.addActionListener(_ctrl.searchButton());
        panel.add(HorizontalBox.create(Box.createHorizontalStrut(20), searchLabel, _searchField, _searchButton, Box.createHorizontalStrut(20)));
        
        panel.add(Box.createVerticalStrut(10));
        
        _refreshButton.addActionListener(_ctrl.refreshButton());
        _deleteButton.addActionListener(_ctrl.deleteButton());
        panel.add(HorizontalBox.create(_refreshButton, _deleteButton));
        
        panel.add(Box.createVerticalStrut(10));
    }
    
    void clearList() {
        _list.clear();
        _list.redraw();
    }

    void addItem(ForumPostItem item){
        _list.add(item);
        _list.redraw();
    }
    
    void addItems(ForumPostItem[] items){
        for (ForumPostItem item : items) {
            _list.add(item);
        }
        _list.redraw();
    }
    
    ArrayList<ForumPostItem> getSelected() {
        ArrayList<ForumPostItem> selected = new ArrayList<ForumPostItem>();
        for (ItemListElement item : _list.getSelected()) {
            selected.add((ForumPostItem) item);
        }
        return selected;
    }
    
    void removeItem(ForumPostItem item) {
        _list.remove(item);
        _list.redraw();
    }

    public String getSearchName() {
        return _searchField.getText();
    }

    public void clearSearchField() {
        _searchField.setText("");
    }

    public void setEnabled(boolean enabled) {
        _searchButton.setEnabled(enabled);
        _refreshButton.setEnabled(enabled);
        _deleteButton.setEnabled(enabled);
        _searchField.setEnabled(enabled);
    }
}
