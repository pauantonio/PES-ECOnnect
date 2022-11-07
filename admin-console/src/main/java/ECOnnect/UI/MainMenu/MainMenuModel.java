package ECOnnect.UI.MainMenu;

import ECOnnect.API.LoginService;
import ECOnnect.API.ServiceFactory;

public class MainMenuModel {
    public void logout() {
        LoginService logoutSv = ServiceFactory.getInstance().getLoginService();
        logoutSv.logout();
    }
}
