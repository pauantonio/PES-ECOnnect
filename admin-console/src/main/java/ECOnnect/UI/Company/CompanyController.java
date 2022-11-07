package ECOnnect.UI.Company;

import ECOnnect.UI.Interfaces.*;
import ECOnnect.UI.Utilities.ExecutionThread;
import ECOnnect.API.CompanyService.Company;
import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Company.Create.NewCompanyScreen;
import ECOnnect.UI.Company.Questions.CompanyQuestionsScreen;

import java.awt.event.*;
import java.util.Collection;

public class CompanyController extends Controller {

    private final CompanyView _view = new CompanyView(this);
    private final CompanyModel _model = new CompanyModel();

    ActionListener addButton() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(NewCompanyScreen.class);
        };
    }
    
    ActionListener questionsButton(){
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(CompanyQuestionsScreen.class);
        };
    }
    
    ActionListener removeButton() {
        return (ActionEvent e) -> {
            Collection<CompanyItem> selected = _view.getSelected();
            
            // Display confirmation dialog
            String companyPlural = selected.size() == 1 ? " company?" : " companies?";
            _view.displayConfirmation("Are you sure you want to delete " + selected.size() + companyPlural, () -> {
                // YES action, delete companies
                for (CompanyItem item : selected) {
                    try {
                        _model.removeCompany(item.getId());
                        _view.deleteItem(item);
                    }
                    catch (Exception ex) {
                        _view.displayError("Could not remove company '" + item.getName() + "':\n" + ex.getMessage());
                    }
                }
            });
        };
    }

    public View getView() {
        return _view;
    }
    
    @Override
    public void postInit(Object[] args) {
        ExecutionThread.nonUI(()->{
          // Get companies from model
            Company[] companies = null;
            try {
                companies = _model.getCompanies();
            }
            catch (Exception e) {
                ExecutionThread.UI(()->{
                    _view.displayError("Could not get companies:\n" + e.getMessage());
                });
                return;
            }
            
            // Convert to company items
            CompanyItem[] companyItems = new CompanyItem[companies.length];
            
            for (int i = 0; i < companies.length; ++i) {
                Company c = companies[i];
                companyItems[i] = new CompanyItem(c);
            }
            
            ExecutionThread.UI(()->_view.addItems(companyItems));
        });
    }
}
