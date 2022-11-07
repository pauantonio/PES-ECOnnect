package ECOnnect.UI.Company.Edit;

import ECOnnect.UI.Interfaces.Controller;
import ECOnnect.UI.Interfaces.View;
import ECOnnect.API.CompanyService.Company;
import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Company.CompanyModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditCompanyController extends Controller implements IEditCompanyController {
    private final EditCompanyView _view = new EditCompanyView(this, "Edit Company");
    private final CompanyModel _model = new CompanyModel();
    
    private int _companyId;

    public View getView(){
        return _view;
    }

    public ActionListener saveButton(){
        return (ActionEvent e) -> {
            String name = _view.getNameText();
            String image = _view.getImageTxt();
            String lat = _view.getLatitudeText();
            String lon = _view.getLongitudeText();
            
            try {
                _model.updateCompany(_companyId, name, image, lat, lon);
            }
            catch (Exception ex) {
                _view.displayError("Could not edit company:\n" + ex.getMessage());
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
    
    @Override
    public void postInit(Object[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected 1 argument: company:Company");
        }
        Company company = (Company) args[0];
        _companyId = company.id;
        
        _view.setFields(company.name, company.imageurl, company.lat, company.lon);
    }
}
