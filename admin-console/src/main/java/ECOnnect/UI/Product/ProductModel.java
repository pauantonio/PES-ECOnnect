package ECOnnect.UI.Product;

import ECOnnect.API.ProductService;
import ECOnnect.API.ServiceFactory;
import ECOnnect.API.ProductService.Product;

public class ProductModel {
        
    // Get products of a product type
    Product[] getProducts(String productType) {
        // Get products from API
        ProductService service = ServiceFactory.getInstance().getProductService();
        // No need to store products in model
        return service.getProducts(productType);
    }
    
    // Get products of all product types
    Product[] getProducts() {
        return getProducts(null);
    }
    
    public void addProduct(String name, String manufacturer, String imageUrl, String type) {
        // Trim whitespace
        name = name.trim();
        manufacturer = manufacturer.trim();
        imageUrl = imageUrl.trim();
        
        checkParams(name, manufacturer, imageUrl, type);
        
        // Add product type to API
        ProductService service = ServiceFactory.getInstance().getProductService();
        service.createProduct(name, manufacturer, imageUrl, type);
    }
    
    public void updateProduct(int id, String name, String manufacturer, String imageUrl, String type) {
        // Trim whitespace
        name = name.trim();
        manufacturer = manufacturer.trim();
        imageUrl = imageUrl.trim();
        
        checkParams(name, manufacturer, imageUrl, type);
        
        // Add product type to API
        ProductService service = ServiceFactory.getInstance().getProductService();
        service.updateProduct(id, name, manufacturer, imageUrl, type);
    }
    
    private void checkParams(String name, String manufacturer, String imageUrl, String type) {
        
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (manufacturer.isEmpty()) {
            throw new IllegalArgumentException("Manufacturer cannot be empty");
        }
        if (imageUrl.isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be empty");
        }
        if (type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be empty");
        }
    }
    

    public void removeProduct(int id) {
        ProductService service = ServiceFactory.getInstance().getProductService();
        service.deleteProduct(id);
    }
}
