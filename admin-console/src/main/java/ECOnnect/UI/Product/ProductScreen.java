package ECOnnect.UI.Product;

import ECOnnect.UI.Interfaces.Screen;

public class ProductScreen extends Screen {
    
    public ProductScreen(){
        super(new ProductController());
    }

    public String getTitle(){
        return "Products of type";
    }
}
