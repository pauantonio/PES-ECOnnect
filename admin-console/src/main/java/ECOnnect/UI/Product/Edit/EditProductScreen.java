package ECOnnect.UI.Product.Edit;

import ECOnnect.UI.Interfaces.Screen;

public class EditProductScreen extends Screen {
    public EditProductScreen(){
        super(new EditProductController());
    }
    public String getTitle(){return "Edit product";}
}
