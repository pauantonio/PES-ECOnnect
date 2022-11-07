package ECOnnect.UI.Product.Create;

import ECOnnect.UI.Interfaces.Controller;
import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Product.ProductModel;
import ECOnnect.UI.Product.Edit.EditProductView;
import ECOnnect.UI.Product.Edit.IEditProductController;
import ECOnnect.UI.ScreenManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewProductController extends Controller implements IEditProductController {
    private EditProductView _view = new EditProductView(this);
    private ProductModel _model = new ProductModel();
    private String _type;

    public View getView() {
        return _view;
    }

    public ActionListener saveButton() {
        return (ActionEvent e) -> {
            String name = _view.getNameText();
            String manufacturer = _view.getManufacturerText();
            String imageUrl = _view.getImageUrlText();

            try{
                _model.addProduct(name, manufacturer, imageUrl, _type);
            }
            catch (Exception ex) {
                _view.displayError("Could not create product:\n" + ex.getMessage());
                return;
            }
            
            ScreenManager.getInstance().show(ScreenManager.PRODUCT_SCREEN);
        };
    }

    public ActionListener cancelButton() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(ScreenManager.PRODUCT_SCREEN);
        };
    }    
    
    @Override
    public void postInit(Object[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected 1 argument: productType:String");
        }
        _type = (String) args[0];
        _view.setTitle("Create new Product of type '" + _type + "'");
    }
}
