package ECOnnect.UI.ProductTypes;

import ECOnnect.UI.Interfaces.Screen;

public class ProductTypesScreen extends Screen {
    public ProductTypesScreen() {
        super(new ProductTypesController());
    }
    
    public String getTitle() {
        return "Product Types";
    }
}
