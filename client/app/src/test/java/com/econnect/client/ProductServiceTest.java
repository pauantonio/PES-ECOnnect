package com.econnect.client;

import org.junit.*;

import com.econnect.API.*;
import com.econnect.API.HttpClient.StubHttpClient;
import com.econnect.API.ProductService.Product;

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
        assert(products.length == 5);
        
        assertEquals(1, products[0].id);
        assertEquals(2, products[1].id);
        assertEquals(3, products[2].id);
        assertEquals(4, products[3].id);
        assertEquals(5, products[4].id);
        
        assertEquals("product1", products[0].getName());
        assertEquals("product2", products[1].getName());
        assertEquals("product3", products[2].getName());
        assertEquals("product4", products[3].getName());
        assertEquals("during", products[4].getName());
        
        assertEquals("manufacturer2", products[1].manufacturer);
        assertEquals("manufacturer3", products[2].manufacturer);
        
        assertEquals("imageUrl3", products[2].imageurl);
        assertEquals("imageUrl4", products[3].imageurl);
        
        assertEquals("type1", products[0].type);
        assertEquals("type2", products[2].type);
    }
    
    @Test
    public void testGetAllProductsWithTypeNotExists() {
        expectException(()->
            sv.getProducts("type3"),
            "The product type type3 does not exist"
        );
    }
    
    @Test
    public void cannotGetProductsWithouthToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.getProducts(null),
            "User token not set"
        );
    }


    @Test
    public void getProductDetailsOk() {
        ProductService.ProductDetails productDetails = sv.getProductDetails(0);

        assertNotNull(productDetails);

        assertEquals("imageUrl0", productDetails.imageURL);
        assertEquals("manufacturer0", productDetails.manufacturer);
        assertEquals("product0", productDetails.name);

        assertEquals(3, productDetails.questions.length);
        assertEquals(12, productDetails.questions[0].num_no);
        assertEquals(11, productDetails.questions[0].num_yes);
        assertEquals("q0", productDetails.questions[0].text);
        assertEquals(22, productDetails.questions[1].num_no);
        assertEquals(21, productDetails.questions[1].num_yes);
        assertEquals("q1", productDetails.questions[1].text);
        assertEquals(32, productDetails.questions[2].num_no);
        assertEquals(31, productDetails.questions[2].num_yes);
        assertEquals("q2", productDetails.questions[2].text);

        assertEquals(6, productDetails.ratings.length);
        assertEquals(1, productDetails.ratings[0]);
        assertEquals(0, productDetails.ratings[1]);
        assertEquals(10, productDetails.ratings[5]);
    }

    @Test
    public void testGetCompanyDetailsNotExists() {
        expectException(() ->
            sv.getProductDetails(2),
            "The product with id 2 does not exist"
        );
    }

    @Test
    public void cannotGetCompanyDetailsWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
            sv.getProductDetails(0),
            "This session has expired, please logout and try again"
        );
    }

    @Test
    public void cannotGetCompanyDetailsWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(() ->
            sv.getProductDetails(0),
            "User token not set"
        );
    }
}