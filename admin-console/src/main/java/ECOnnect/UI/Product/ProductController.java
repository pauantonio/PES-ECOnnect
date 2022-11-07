package ECOnnect.UI.Product;

import ECOnnect.UI.Interfaces.Controller;
import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.Product.Create.NewProductScreen;
import ECOnnect.UI.Utilities.ExecutionThread;
import ECOnnect.API.ProductService.Product;
import ECOnnect.UI.ScreenManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class ProductController extends Controller {
    private final ProductView _view = new ProductView(this);
    private final ProductModel _model = new ProductModel();
    private static String _type;

    ActionListener addButton() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(NewProductScreen.class, _type);
        };
    }
    
    ActionListener backButton(){
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(ScreenManager.MAIN_MENU_SCREEN);
        };
    }
    
    ActionListener removeButton() {
        return (ActionEvent e) -> {
            Collection<ProductItem> selected = _view.getSelected();
            
            // Display confirmation dialog
            String productPlural = selected.size() == 1 ? " product?" : " products?";
            _view.displayConfirmation("Are you sure you want to delete " + selected.size() + productPlural, () -> {
                // YES action, delete companies
                for (ProductItem item : selected) {
                    try {
                        _model.removeProduct(item.getId());
                        _view.deleteItem(item);
                    }
                    catch (Exception ex) {
                        _view.displayError("Could not remove product '" + item.getName() + "':\n" + ex.getMessage());
                    }
                }
            });
        };
    }
    
    @Override
    public View getView(){
        return _view;
    }
    
    @Override
    public void postInit(Object[] args) {
        if (args.length > 1) {
            throw new IllegalArgumentException("Expected 1 argument: productType:String (or 0 arguments to keep previous type)");
        }
        if (args.length == 1) {
            _type = (String) args[0];
        }
        
        ScreenManager.getInstance().updateTitle("Products of type '" + _type + "'");
        
        ExecutionThread.nonUI(()->{
            try {
                // Get products from model
                Product[] products = _model.getProducts(_type);
                // Convert to product items
                ProductItem[] productItems = new ProductItem[products.length];
                
                for (int i = 0; i < products.length; ++i) {
                    Product p = products[i];
                    productItems[i] = new ProductItem(p);
                }
                
                ExecutionThread.UI(()->{
                    _view.addItems(productItems); 
                });
            }
            catch (Exception e) {
                ExecutionThread.UI(()->{
                    _view.displayError("Could not get products:\n" + e.getMessage());
                });
            }
        });
    }
}
