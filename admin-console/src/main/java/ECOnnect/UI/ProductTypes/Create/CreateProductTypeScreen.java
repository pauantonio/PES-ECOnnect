package ECOnnect.UI.ProductTypes.Create;

import ECOnnect.UI.Interfaces.Screen;

public class CreateProductTypeScreen extends Screen {
    public CreateProductTypeScreen() {
        super(new CreateProductTypeController());
    }
    
    public String getTitle() {
        return "Create new Product Type";
    }
}
