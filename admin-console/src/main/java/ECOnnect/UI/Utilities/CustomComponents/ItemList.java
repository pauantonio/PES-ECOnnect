package ECOnnect.UI.Utilities.CustomComponents;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;

public class ItemList<T extends ItemListElement> extends JScrollPane {
    private final JPanel _list = new JPanel();
    private final String[] _headerNames;
    private final Integer[] _widths;

    public ItemList(String[] headerNames, Integer[] columnWidths) {
        if (headerNames.length != columnWidths.length) {
            throw new IllegalArgumentException("Header names and column widths must be the same length");
        }
        _headerNames = headerNames;
        _widths = columnWidths;
        _list.setLayout(new BoxLayout(_list, BoxLayout.PAGE_AXIS));
        
        super.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        super.getVerticalScrollBar().setUnitIncrement(5);
        super.setViewportView(_list);
        
        // Listen for mouse movements to load item listeners on demand
        _list.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent evt) {
                Component comp = _list.getComponentAt(evt.getPoint());
                if (comp instanceof ItemListElement) {
                    ((ItemListElement) comp).lazyUpdate();
                }
            }
        });
        
        addHeader();
    }
    
    private void addHeader() {
        final JPanel headerPanel = new JPanel();
        final int HEIGHT = ItemListElement.DEFAULT_SIZE.height;
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.LINE_AXIS));
        headerPanel.setMaximumSize(ItemListElement.DEFAULT_SIZE);
        headerPanel.setMinimumSize(ItemListElement.DEFAULT_SIZE);
        headerPanel.setPreferredSize(ItemListElement.DEFAULT_SIZE);
        _list.add(headerPanel);
        
        for (int i = 0; i < _headerNames.length; i++) {
            final JTextField text = new JTextField(_headerNames[i]);
            text.setEditable(false);
            text.setFont(new Font(Font.MONOSPACED, Font.PLAIN,  13));
            text.setHorizontalAlignment(SwingConstants.LEFT);
            
            Dimension size = new Dimension(_widths[i], HEIGHT);
            text.setMaximumSize(size);
            text.setMinimumSize(size);
            
            headerPanel.add(text);
        }
    }
    
    // Redraw the UI
    public void redraw() {
        super.revalidate();
        super.repaint();
    }
    
    
    // Add an item to the list
    public void add(T item) {
        _list.add(item);
    }
    
    // Return the item at the specified index
    public T get(int index) {
        @SuppressWarnings("unchecked")
        T item = (T) _list.getComponent(index + 1);
        return item;
    }
    
    // Remove an item from the list
    public void remove(T item) {
        _list.remove(item);
    }
    
    // Remove all items from the list
    public void clear() {
        _list.removeAll();
        addHeader();
    }
    
    // Return the number of elements in the list
    public int length() {
        // Remove header
        return _list.getComponentCount() - 1;
    }
    
    
    // SELECTING ITEMS
    
    // Return all selected items
    public Collection<T> getSelected() {
        ArrayList<T> selected = new ArrayList<>();

        // Start at 1 to avoid the headers panel
        for (int i = 1; i < _list.getComponentCount(); ++i) {
            @SuppressWarnings("unchecked")
            T itemStruct = (T) _list.getComponent(i);
            
            if (itemStruct.isSelected()) {
                selected.add(itemStruct);
            }
        }
        
        return selected;
    }
    
    // Unselect all items
    public void clearSelection() {
        // Start at 1 to avoid the headers panel
        for (int i = 1; i < _list.getComponentCount(); ++i) {
            @SuppressWarnings("unchecked")
            T itemStruct = (T) _list.getComponent(i);
            itemStruct.uncheck();
        }
    }
    
    // Remove all selected items from the list and return them
    public Collection<T> removeSelected() {
        Collection<T> selected = getSelected();
        for (var str : selected) remove(str);
        
        return selected;
    }
}
