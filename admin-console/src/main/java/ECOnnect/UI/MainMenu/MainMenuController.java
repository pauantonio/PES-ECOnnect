package ECOnnect.UI.MainMenu;

import javax.swing.*;
import javax.swing.event.*;

import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Company.CompanyScreen;
import ECOnnect.UI.Forum.ForumScreen;
import ECOnnect.UI.Interfaces.Controller;
import ECOnnect.UI.Interfaces.Screen;
import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.ProductTypes.ProductTypesScreen;

public class MainMenuController extends Controller {
    final Screen[] TAB_SCREENS = {
        new ProductTypesScreen(),
        new CompanyScreen(),
        new ForumScreen()
    };
    
    private final MainMenuView _view = new MainMenuView(this);
    private final MainMenuModel _model = new MainMenuModel();
    
    private static int _currentTabIndex = 0;
    
    private static void setTabIndex(int index) {
        _currentTabIndex = index;
    }
    
    ChangeListener tabListener() {
        return (ChangeEvent changeEvent) -> {
            JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            setTabIndex(sourceTabbedPane.getSelectedIndex());
            String title = sourceTabbedPane.getTitleAt(_currentTabIndex);
            
            ScreenManager.getInstance().updateTitle(title);
            // Special case for the logout tab
            if (title.equals("Logout")) {
                setTabIndex(0);
                logout();
                ScreenManager.getInstance().show(ScreenManager.LOGIN_SCREEN);
            }
        };
    }
    
    private void logout() {
        try {
            _model.logout();
        }
        catch (Exception e) {
            _view.displayWarning("Failed to logout due to an error:\n" + e.getMessage() + "\nReturning to login screen...");
        }
    }
    
    public View getView() {
        return _view;
    }
    
    @Override
    public void postInit(Object[] args) {
        // Set the title to the first tab
        String title = TAB_SCREENS[_currentTabIndex].getTitle();
        ScreenManager.getInstance().updateTitle(title);
        
        // Call the postInit method of all tabs, without arguments
        for (Screen screen : TAB_SCREENS) {
            screen.postInit(new Object[] {});
        }
        
        // Navigate to the correct tab
        _view.setTab(_currentTabIndex);
    }
}
