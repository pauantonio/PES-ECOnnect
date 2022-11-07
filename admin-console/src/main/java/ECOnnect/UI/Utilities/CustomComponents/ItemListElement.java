package ECOnnect.UI.Utilities.CustomComponents;

import java.awt.*;

import javax.swing.*;

public abstract class ItemListElement extends JPanel {
    
    public static final Dimension DEFAULT_SIZE = new Dimension(Integer.MAX_VALUE, 35);
    
    // Call this method AFTER the constructor of the subclass
    protected void init() {
        super.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        super.setMaximumSize(DEFAULT_SIZE);
        super.setMinimumSize(DEFAULT_SIZE);
        super.setPreferredSize(DEFAULT_SIZE);
        
        Component[] rowComponents = getRowComponents();
        Integer[] columnWidths = getColumnWidths();
        if (rowComponents.length != columnWidths.length) {
            throw new IllegalArgumentException("Row components and column widths must be the same length");
        }
        
        for (int i = 0; i < rowComponents.length; i++) {
            final Component component = rowComponents[i];
            final Dimension size = new Dimension(columnWidths[i], DEFAULT_SIZE.height);
            
            component.setMaximumSize(size);
            component.setMinimumSize(size);
            super.add(component);
        }
    }
    
    protected void redraw() {
        super.revalidate();
        super.repaint();
        getParent().revalidate();
        getParent().repaint();
    }
    
    // Override this method to return the components that will be added to the row, and the widths of each column
    protected abstract Component[] getRowComponents();
    protected abstract Integer[] getColumnWidths();
    
    // By default, an item is not selectable
    public boolean isSelected() {
        return false;
    }
    
    // By default, an item is not selectable
    public void uncheck() {
        // Do nothing
    }
    
    
    // Load listeners on demand to improve performance
    @Override
    public void addNotify() {
        // Do nothing (loads the list quickly, but the user cannot interact with it)
    }
    // Call this method to load the listeners
    public void lazyUpdate() {
        super.addNotify();
    }
}
