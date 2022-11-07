package ECOnnect.UI.Login;

import ECOnnect.UI.Interfaces.Screen;

public class LoginScreen extends Screen {
    
    public LoginScreen() {
        super(new LoginController());
    }
    
    public String getTitle() {
        return "Login";
    }
}
