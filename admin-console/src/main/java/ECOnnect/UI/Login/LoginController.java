package ECOnnect.UI.Login;

import java.awt.event.*;

import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Interfaces.*;
import ECOnnect.UI.Utilities.ExecutionThread;

public class LoginController extends Controller {
    
    private final LoginView _view = new LoginView(this);
    private final LoginModel _model = new LoginModel();
    
    public LoginController() {
        // Add hook for doing logout on window close
        ScreenManager.getInstance().addClosingListener(() -> {
            try {
                _model.logout();
            }
            catch (Exception e) {
                // May fail if the user was not logged in
            }
        });
    }
    
    public ActionListener loginButton() {
        return (ActionEvent e) -> {
            String email = _view.getUsernameText();
            String password = _view.getPasswordText();
            _view.enableInput(false);
            
            // This could take some time, invoke in a non-UI thread
            ExecutionThread.nonUI(() -> {
                try {
                    _model.validate(email, password);
                }
                catch (Exception ex) {
                    ExecutionThread.UI(() -> {
                        _view.enableInput(true);
                        _view.displayError("There has been an error:\n" + ex.getMessage());
                    });
                    return;
                }
                // Return to UI thread
                ExecutionThread.UI(() -> {
                    ScreenManager.getInstance().show(ScreenManager.MAIN_MENU_SCREEN);
                });
            });
        };
    }
    
    public View getView() {
        return _view;
    }
    
    @Override
    public void postInit(Object[] args) {
        // Ping Heroku at startup, so that it starts to wake up and login is faster
        ExecutionThread.nonUI(() -> _model.pingServer());
    }
}
