package com.econnect.client;

import org.junit.*;

import com.econnect.API.*;
import com.econnect.API.HttpClient.StubHttpClient;

import static org.junit.Assert.*;

public class ProductTypesServiceTest {
    ProductTypesService sv;
   
    @Before
    public void setUp() {
        sv = ServiceFactory.getInstance().getProductTypesService();
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
    public void testGetProductTypesOk() {
        ProductTypesService.ProductType[] types = sv.getProductTypes();
        // This should not throw an exception
        assertNotNull(types);
        assert(types.length == 2);
        
        assertEquals("type1", types[0].name);
        assertEquals("type2", types[1].name);
        
        assertEquals("q1", types[0].questions[0].statement);
        assertEquals("q2", types[0].questions[1].statement);
        assertEquals("q6", types[1].questions[2].statement);
        assertEquals(2, types[0].questions[1].questionid);
        assertEquals(5, types[1].questions[1].questionid);
    }
    
    @Test
    public void cannotGetProductsWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.getProductTypes(),
            "User token not set"
        );
    }
    
    @Test
    public void cannotGetProductsWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.getProductTypes(),
            "This session has expired, please logout and try again"
        );
    }

}
