package ECOnnect.API;

import java.util.TreeMap;

import ECOnnect.API.Exceptions.ApiException;

public class ProductService extends Service {
    
    // Only allow instantiating from ServiceFactory
    ProductService() {}
    
    public class Product {
        // Important: The name of these attributes must match the ones in the returned JSON
        public final int id;
        public final String name;
        public final float avgrating;
        public final String manufacturer;
        public final String imageurl;
        public final String type;
        
        public Product(int id, String name, float avgRating, String manufacturer, String imageURL, String type) {
            this.id = id;
            this.name = name;
            this.avgrating = avgRating;
            this.manufacturer = manufacturer;
            this.imageurl = imageURL;
            this.type = type;
        }
    }
    
    // Get product of specific type (or all products if type is null)
    public Product[] getProducts(String type) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        // Empty string means all products
        if (type == null) type = "";
        params.put(ApiConstants.PRODUCT_TYPE, type);
        
        
        JsonResult result = null;
        try {
            // Call API
            super.needsToken = true;
            result = get(ApiConstants.PRODUCTS_PATH, params);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_TYPE_NOT_EXISTS:
                    throw new RuntimeException("The product type " + type + " does not exist");
                default:
                    throw e;
            }
        }
        
        // Parse result
        Product[] products = result.getArray(ApiConstants.RET_RESULT, Product[].class);
        assertResultNotNull(products, result);
        return products;
    }
    
    // Create a new product
    public void createProduct(String name, String manufacturer, String imageURL, String type) {
        try {
            // Call API
            updateProductImpl(name, imageURL, type, manufacturer, ApiConstants.PRODUCTS_PATH);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_TYPE_NOT_EXISTS:
                    throw new RuntimeException("The product type " + type + " does not exist");
                case ApiConstants.ERROR_PRODUCT_EXISTS:
                    throw new RuntimeException("The product " + name + " already exists");
                default:
                    throw e;
            }
        }
    }
    
    // Update an existing product
    public void updateProduct(int id, String name, String manufacturer, String imageURL, String type) {
        try {
            // Call API
            updateProductImpl(name, imageURL, type, manufacturer, ApiConstants.PRODUCTS_PATH + "/" + id);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_PRODUCT_NOT_EXISTS:
                    throw new RuntimeException("The product with id " + id + " does not exist");
                case ApiConstants.ERROR_PRODUCT_EXISTS:
                    throw new RuntimeException("There is already a product with name " + name);
                case ApiConstants.ERROR_TYPE_NOT_EXISTS:
                    throw new RuntimeException("The product type " + type + " does not exist");
                default:
                    throw e;
            }
        }
    }
    
    public void updateProductImpl(String name, String imageURL, String type, String manufacturer, String path) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.PRODUCT_NAME, name);
        params.put(ApiConstants.PRODUCT_MANUFACTURER, manufacturer);
        params.put(ApiConstants.PRODUCT_IMAGE_URL, imageURL);
        params.put(ApiConstants.PRODUCT_TYPE, type);
        
        // Call API
        super.needsToken = true;
        JsonResult result = post(path, params, null);
        
        expectOkStatus(result);
    }
    
    // Delete a product
    public void deleteProduct(int id) {
        JsonResult result = null;
        try {
            // Call API
            super.needsToken = true;
            result = delete(ApiConstants.PRODUCTS_PATH + "/" + id, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_PRODUCT_NOT_EXISTS:
                    throw new RuntimeException("The product with id " + id + " does not exist");
                default:
                    throw e;
            }
        }
        
        expectOkStatus(result);
    }
}
