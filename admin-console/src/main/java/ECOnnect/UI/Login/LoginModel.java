package ECOnnect.UI.Login;

import ECOnnect.API.LoginService;
import ECOnnect.API.ServiceFactory;

public class LoginModel {
    
    boolean validate(String email, String password) {
        // Perform local validation
        email = email.trim();
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        LoginService loginSv = ServiceFactory.getInstance().getLoginService();
        loginSv.login(email, password);
        
        return true;
    }
    
    public void logout() {
        LoginService logoutSv = ServiceFactory.getInstance().getLoginService();
        logoutSv.logout();
    }

    public void pingServer() {
        LoginService servive = ServiceFactory.getInstance().getLoginService();
        servive.pingServer();
    }
}
