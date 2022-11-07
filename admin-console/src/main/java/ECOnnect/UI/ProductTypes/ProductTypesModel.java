package ECOnnect.UI.ProductTypes;

import java.util.ArrayList;
import java.util.Arrays;

import ECOnnect.API.ProductTypesService;
import ECOnnect.API.ServiceFactory;
import ECOnnect.API.ProductTypesService.ProductType;

public class ProductTypesModel {
    
    private static ArrayList<ProductType> _productTypes = null;
    
    // Return all product types from the API and store them in the model
    public ProductType[] getProductTypes() {
        // Get product types from API
        ProductTypesService service = ServiceFactory.getInstance().getProductTypesService();
        ProductType[] pt = service.getProductTypes();
        
        // Store in model
        _productTypes = new ArrayList<>(Arrays.asList(pt));
        
        // Return product types
        return pt;
    }
    
    public ProductType getType(int index) {
        return _productTypes.get(index);
    }
    
    public void addProductType(String name, String[] questions) {
        
        name = name.trim();
        
        // Local validation
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (questions.length == 0) {
            throw new IllegalArgumentException("Must have at least one question");
        }
        
        // Add product type to API
        ProductTypesService service = ServiceFactory.getInstance().getProductTypesService();
        service.createProductType(name, questions);
    }
    
    public void deleteProductType(String name) {
        ProductTypesService service = ServiceFactory.getInstance().getProductTypesService();
        service.deleteProductType(name);
    }
    
    
    public void renameProductType(String oldName, String newName) {
        ProductTypesService service = ServiceFactory.getInstance().getProductTypesService();
        service.renameType(oldName, newName);
    }
    
    public void createQuestion(String typeName) {
        ProductTypesService service = ServiceFactory.getInstance().getProductTypesService();
        service.createQuestion(typeName, "");
    }
}
