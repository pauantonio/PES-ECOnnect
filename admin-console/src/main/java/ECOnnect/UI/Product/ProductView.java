package ECOnnect.UI.Product;

import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Utilities.HorizontalBox;
import ECOnnect.UI.Utilities.CustomComponents.ItemList;

import java.util.Collection;

import javax.swing.*;

public class ProductView extends View {
    private final ProductController _ctrl;
    private ItemList<ProductItem> _list;

    // Components

    public ProductView(ProductController ctrl) {
        this._ctrl = ctrl;
        setUp();
    }

    private void setUp() {
        _list = new ItemList<>(ProductItem.getHeaderNames(), ProductItem.getWidths());
        panel.add(_list);

        panel.add(Box.createVerticalStrut(10));
        
        JButton backButton = new JButton("Go back");
        backButton.addActionListener(_ctrl.backButton());
        JButton addButton = new JButton("Add new");
        addButton.addActionListener(_ctrl.addButton());
        JButton removeButton = new JButton("Remove selected");
        removeButton.addActionListener(_ctrl.removeButton());
        panel.add(HorizontalBox.create(backButton, addButton, removeButton));

        panel.add(Box.createVerticalStrut(10));
    }

    void addItem(ProductItem item) {
        _list.add(item);
        _list.redraw();
    }
    
    void addItems(ProductItem[] items) {
        for (ProductItem item : items) {
            _list.add(item);
        }
        _list.redraw();
    }
    
    Collection<ProductItem> getSelected() {
        return _list.getSelected();
    }
    
    void deleteItem(ProductItem item){
        _list.remove(item);
        _list.redraw();
    }

}
