package ECOnnect.UI.MainMenu;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import ECOnnect.UI.Interfaces.*;

public class MainMenuView extends View {
    
    private final MainMenuController _ctrl;
    private final JTabbedPane _tabbedPane = new JTabbedPane();
    
    public MainMenuView(MainMenuController ctrl) {
        this._ctrl = ctrl;
        setUp();
    }
    
    // Build the GUI
    private void setUp() {
        _tabbedPane.setUI(new CustomTabbedPaneUI());
        
        // Create tabs
        for (Screen screen : _ctrl.TAB_SCREENS) {
            _tabbedPane.addTab(screen.getTitle(), screen.getPanel());
        }
        _tabbedPane.addTab("Logout", null);
        _tabbedPane.addChangeListener(_ctrl.tabListener());
        panel.add(_tabbedPane);
    }
    
    void setTab(int index) {
        _tabbedPane.setSelectedIndex(index);
    }
    
    // Custom TabbedPaneUI that moves the Logout tab to the right
    private class CustomTabbedPaneUI extends BasicTabbedPaneUI {
        @Override
        protected java.awt.LayoutManager createLayoutManager() {
            return new TabbedPaneLayout() {
                @Override
                protected void calculateTabRects(int tabPlacement, int tabCount) {
                    if (tabCount == 0) return;
                    // Compute coordinates for each tab
                    super.calculateTabRects(tabPlacement,tabCount);
                    // Override the last tab's coordinates (move it to the right)
                    rects[rects.length-1].x = panel.getWidth() - rects[rects.length-1].width - 10;
                }
            };
        }
    }
}
