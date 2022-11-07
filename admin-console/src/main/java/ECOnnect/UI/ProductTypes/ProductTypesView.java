package ECOnnect.UI.ProductTypes;

import java.util.Collection;

import javax.swing.*;

import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.HorizontalBox;
import ECOnnect.UI.Utilities.CustomComponents.ItemList;

public class ProductTypesView extends View {
    
    private final ProductTypesController _ctrl;
    private ItemList<ProductTypeItem> _list;
    
    
    public ProductTypesView(ProductTypesController ctrl) {
        this._ctrl = ctrl;
        setUp();
    }
    
    // Build the GUI
    private void setUp() {
        _list = new ItemList<>(ProductTypeItem.getHeaderNames(), ProductTypeItem.getWidths());
        panel.add(_list);
        
        panel.add(Box.createVerticalStrut(10));
        
        JButton addButton = new JButton("Add new");
        addButton.addActionListener(_ctrl.addButton());
        JButton deleteButton = new JButton("Delete selected");
        deleteButton.addActionListener(_ctrl.deleteButton());
        panel.add(HorizontalBox.create(addButton, deleteButton));
        
        panel.add(Box.createVerticalStrut(10));
    }
    
    void addItem(ProductTypeItem item) {
        _list.add(item);
        _list.redraw();
    }
    
    void addItems(ProductTypeItem[] items) {
        for (ProductTypeItem item : items) {
            _list.add(item);
        }
        _list.redraw();
    }
    
    Collection<ProductTypeItem> getSelectedItems() {
        return _list.getSelected();
    }
    
    void removeItem(ProductTypeItem item) {
        _list.remove(item);
        _list.redraw();
    }

}
