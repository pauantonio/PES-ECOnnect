package ECOnnect.UI.Company.Create;

import ECOnnect.UI.Interfaces.Controller;
import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Company.CompanyModel;
import ECOnnect.UI.Company.Edit.EditCompanyView;
import ECOnnect.UI.Company.Edit.IEditCompanyController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewCompanyController extends Controller implements IEditCompanyController {
    private final EditCompanyView _view = new EditCompanyView(this, "New Company");
    private final CompanyModel _model = new CompanyModel();

    public View getView(){
        return _view;
    }

    public ActionListener saveButton(){
        return (ActionEvent e) -> {
            String name = _view.getNameText();
            String image = _view.getImageTxt();            
            
            try {
                _model.addCompany(name, image, _view.getLatitudeText(), _view.getLongitudeText());
            }
            catch (Exception ex) {
                _view.displayError("Could not create company:\n" + ex.getMessage());
                return;
            }
            
            ScreenManager.getInstance().show(ScreenManager.MAIN_MENU_SCREEN);
        };
    }

    public ActionListener cancelButton() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(ScreenManager.MAIN_MENU_SCREEN);
        };
    }
}
