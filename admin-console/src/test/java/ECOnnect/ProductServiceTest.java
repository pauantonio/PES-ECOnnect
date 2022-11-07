package ECOnnect;

import org.junit.*;

import ECOnnect.API.*;
import ECOnnect.API.ProductService.Product;

import static org.junit.Assert.*;

public class ProductServiceTest {
    ProductService sv;
   
    @Before
    public void setUp() {
        sv = ServiceFactory.getInstance().getProductService();
        ServiceTestHelper.injectHttpClient(new StubHttpClient());
        ServiceTestHelper.setToken();
    }
    
    private void expectException(Runnable r, String expectedMessage) {
        try {
            r.run();
            fail("Should have thrown an exception with message: " + expectedMessage);
        }
        catch (Exception e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    
    @Test
    public void testGetAllProductsOk() {
        Product[] products = sv.getProducts(null);
        // This should not throw an exception
        assertNotNull(products);
        assert(products.length == 4);
        
        assertEquals(1, products[0].id);
        assertEquals(2, products[1].id);
        assertEquals(3, products[2].id);
        assertEquals(4, products[3].id);
        
        assertEquals("product1", products[0].name);
        assertEquals("product2", products[1].name);
        assertEquals("product3", products[2].name);
        assertEquals("product4", products[3].name);
        
        assertEquals("manufacturer2", products[1].manufacturer);
        assertEquals("manufacturer3", products[2].manufacturer);
        
        assertEquals("imageUrl3", products[2].imageurl);
        assertEquals("imageUrl4", products[3].imageurl);
        
        assertEquals("type1", products[0].type);
        assertEquals("type2", products[2].type);
    }
    
    @Test
    public void testGetAllProductsWithTypeOk() {
        Product[] products = sv.getProducts("type1");
        // This should not throw an exception
        assertNotNull(products);
        assert(products.length == 2);
        
        assertEquals(1, products[0].id);
        assertEquals(2, products[1].id);
        
        assertEquals("product1", products[0].name);
        assertEquals("product2", products[1].name);
        
        assertEquals("manufacturer2", products[1].manufacturer);
        
        assertEquals("imageUrl2", products[1].imageurl);
        
        assertEquals("type1", products[0].type);
    }
    
    @Test
    public void testGetAllProductsWithTypeNotExists() {
        expectException(()->
            sv.getProducts("type3"),
            "The product type type3 does not exist"
        );
    }
    
    @Test
    public void cannotGetProductsWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.getProducts(null),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotGetProductsWithouthToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.getProducts(null),
            "Admin token not set"
        );
    }
    
    
    @Test
    public void testCreateProductOk() {
        sv.createProduct("newProduct", "manufacturer", "imageUrl", "type1");
        // This should not throw an exception
    }
    
    @Test
    public void cannotCreateExistingProduct() {
        expectException(()->
            sv.createProduct("existingProduct", "manufacturer", "imageUrl", "type1"),
            "The product existingProduct already exists"
        );
    }
    
    @Test
    public void cannotCreateProductWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.createProduct("newProduct", "manufacturer", "imageUrl", "type1"),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotCreateProductWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.createProduct("newProduct", "manufacturer", "imageUrl", "type1"),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotCreateProductWithTypeNotExists() {
        expectException(()->
            sv.createProduct("newProduct", "manufacturer", "imageUrl", "type3"),
            "The product type type3 does not exist"
        );
    }
    
    
    @Test
    public void testUpdateProductOk() {
        sv.updateProduct(1, "newProduct", "manufacturer", "imageUrl", "type1");
        // This should not throw an exception
    }
    
    @Test
    public void testUpdateProductNotExists() {
        expectException(()->
            sv.updateProduct(2, "newProduct", "manufacturer", "imageUrl", "type1"),
            "The product with id 2 does not exist"
        );
    }
    
    @Test
    public void testUpdateProductAlreadyExists() {
        expectException(()->
            sv.updateProduct(1, "existingProduct", "manufacturer", "imageUrl", "type1"),
            "There is already a product with name existingProduct"
        );
    }
    
    @Test
    public void cannotUpdateProductWithTypeNotExists() {
        expectException(()->
            sv.updateProduct(1, "newProduct", "manufacturer", "imageUrl", "type3"),
            "The product type type3 does not exist"
        );
    }
    
    @Test
    public void cannotUpdateProductWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.updateProduct(1, "newProduct", "manufacturer", "imageUrl", "type1"),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotUpdateProductWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.updateProduct(1, "newProduct", "manufacturer", "imageUrl", "type1"),
            "Admin token not set"
        );
    }
    
    
    @Test
    public void testDeleteProductOk() {
        sv.deleteProduct(1);
        // This should not throw an exception
    }
    
    @Test
    public void testDeleteProductNotExists() {
        expectException(()->
            sv.deleteProduct(2),
            "The product with id 2 does not exist"
        );
    }
    
    @Test
    public void cannotDeleteProductWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.deleteProduct(1),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotDeleteProductWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.deleteProduct(1),
            "Admin token not set"
        );
    }
    
}