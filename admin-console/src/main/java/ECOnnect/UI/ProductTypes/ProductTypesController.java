package ECOnnect.UI.ProductTypes;

import java.awt.event.*;
import java.util.Collection;

import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Interfaces.*;
import ECOnnect.UI.ProductTypes.Create.CreateProductTypeScreen;
import ECOnnect.UI.ProductTypes.ProductTypeItem.IProductTypeItemCallback;
import ECOnnect.UI.ProductTypes.Questions.ProductTypeQuestionsScreen;
import ECOnnect.UI.Utilities.ExecutionThread;
import ECOnnect.API.ProductTypesService.ProductType;
import ECOnnect.API.ProductTypesService.ProductType.Question;

public class ProductTypesController extends Controller {
    private final ProductTypesView _view = new ProductTypesView(this);
    private final ProductTypesModel _model = new ProductTypesModel();
    
    ActionListener addButton() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(CreateProductTypeScreen.class);
        };
    }
    
    ActionListener deleteButton() {
        return (ActionEvent e) -> {
            Collection<ProductTypeItem> selected = _view.getSelectedItems();
            
            // Display confirmation dialog
            String typePlural = selected.size() == 1 ? " product type?" : " product types?";
            _view.displayConfirmation("Are you sure you want to delete " + selected.size() + typePlural, () -> {
                // YES action, delete companies
                for (ProductTypeItem item : selected) {
                    try {
                        _model.deleteProductType(item.getName());
                        _view.removeItem(item);
                    }
                    catch (Exception ex) {
                        _view.displayError("Could not remove product type '" + item.getName() + "':\n" + ex.getMessage());
                    }
                }
            });
        };
    }
    
    @Override
    public void postInit(Object[] args) {
        ExecutionThread.nonUI(()->{
            try {
                // Get types from model
                ProductType[] items = _model.getProductTypes();
                
                // Convert to type items
                ProductTypeItem[] productTypeItems = new ProductTypeItem[items.length];
                
                for (int i = 0; i < items.length; ++i) {
                    productTypeItems[i] = new ProductTypeItem(_callback, i, items[i]);
                }
                
                ExecutionThread.UI(()->_view.addItems(productTypeItems));
            }
            catch (Exception e) {
                ExecutionThread.UI(()-> {
                    _view.displayError("Could not fetch existing types: " + e.getMessage());
                });
            } 
        });
    }
    
    
    private IProductTypeItemCallback _callback = new IProductTypeItemCallback() {
        @Override
        public void viewQuestions(int index) {
            String type = _model.getType(index).name;
            Question[] questions = _model.getType(index).questions;
            ScreenManager.getInstance().show(ProductTypeQuestionsScreen.class, type, questions);
        }
        
        @Override
        public void viewProducts(int index) {
            String type = _model.getType(index).name;
            ScreenManager.getInstance().show(ScreenManager.PRODUCT_SCREEN, type);
        }

        @Override
        public boolean renameType(int index, String newName) {
            String oldName = _model.getType(index).name;
            try {
                _model.renameProductType(oldName, newName);
                return true;
            }
            catch (Exception e) {
                _view.displayError("Could not rename product type '" + oldName + "' to '" + newName + "':\n" + e.getMessage());
                return false;
            }
        }
    };    

    
    public View getView() {
        return _view;
    }
}
