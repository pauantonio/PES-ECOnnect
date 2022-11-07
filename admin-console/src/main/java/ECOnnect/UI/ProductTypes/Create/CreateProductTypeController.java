package ECOnnect.UI.ProductTypes.Create;

import java.awt.event.*;
import java.util.ArrayList;

import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Interfaces.Controller;
import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.ProductTypes.ProductTypesModel;

public class CreateProductTypeController extends Controller {
    private final CreateProductTypeView _view = new CreateProductTypeView(this);
    private final ProductTypesModel _model = new ProductTypesModel();
    
    
    ActionListener okButton() {
        return (ActionEvent e) -> {
            String name = _view.getName();
            String[] questions = splitQuestions(_view.getQuestions());
            
            // Add product type to backend
            try {
                _model.addProductType(name, questions);
            }
            catch (Exception ex) {
                _view.displayWarning("Failed to add product type due to an error:\n" + ex.getMessage());
                return;
            }
            
            // Go back to product types screen
            ScreenManager.getInstance().show(ScreenManager.MAIN_MENU_SCREEN);
        };
    }
    
    ActionListener cancelButton() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(ScreenManager.MAIN_MENU_SCREEN);
        };
    }
    
    
    private String[] splitQuestions(String questions) {
        // Split questions by new line, remove empty lines and trim whitespace
        ArrayList<String> list = new ArrayList<>();
        for (String s : questions.split("\n")) {
            if (!s.isEmpty()) {
                list.add(s.trim());
            }
        }
        return list.toArray(new String[0]);
    }
    
    @Override
    public View getView() {
        return _view;
    }
}
