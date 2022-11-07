package ECOnnect.UI.MainMenu;

import ECOnnect.UI.Interfaces.Screen;

public class MainMenuScreen extends Screen {
    public MainMenuScreen() {
        super(new MainMenuController());
    }
    
    public String getTitle() {
        return "Main Menu";
    }
}
