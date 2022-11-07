package ECOnnect;

import org.junit.*;

import ECOnnect.API.*;

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
            "Admin token not set"
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
    
    
    @Test
    public void testCreateProductTypeOk() {
        sv.createProductType("newType", new String[]{"q1", "q2"});
        // This should not throw an exception
        
        // Test question checker
        expectException(()->
            sv.createProductType("newType", new String[]{"q1", "q2", "abc"}),
            "The server responded with error code incorrect questions"
        );
    }
    
    @Test
    public void testCreateEmptyProductType() {
        sv.createProductType("emptyType", new String[]{});
        // This should not throw an exception
        
        expectException(()->
            sv.createProductType("emptyType", new String[]{"q1", "q2"}),
            "The server responded with error code incorrect amount of questions"
        );
    }
    
    @Test
    public void testCreateExistingProductType() {
        expectException(()->
            sv.createProductType("existingType", new String[]{"q1", "q2"}),
            "There is already a product type with this name"
        );
    }
    
    @Test
    public void cannotCreateProductTypeWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.createProductType("newType", new String[]{"q1", "q2"}),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotCreateProductTypeWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.createProductType("newType", new String[]{"q1", "q2"}),
            "This session has expired, please logout and try again"
        );
    }
    
    
    @Test
    public void testDeleteProductTypeOk() {
        sv.deleteProductType("type1");
        // This should not throw an exception
    }
    
    @Test
    public void testDeleteNonExistingProductType() {
        expectException(()->
            sv.deleteProductType("type3"),
            "The product type type3 does not exist"
        );
    }
    
    @Test
    public void cannotDeleteProductTypeWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.deleteProductType("type1"),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotDeleteProductTypeWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.deleteProductType("type1"),
            "This session has expired, please logout and try again"
        );
    }
    
    
    @Test
    public void testRenameTypeOk() {
        sv.renameType("type1", "newType");
        // This should not throw an exception
    }
    
    @Test
    public void testRenameNonExistingType() {
        expectException(()->
            sv.renameType("type3", "newType"),
            "The product type type3 does not exist"
        );
    }
    
    @Test
    public void testRenameToExistingName() {
        expectException(()->
            sv.renameType("type1", "existingType"),
            "There is already a product type called existingType"
        );
    }
    
    @Test
    public void cannotRenameTypeWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.renameType("type1", "newType"),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotRenameTypeWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.renameType("type1", "newType"),
            "This session has expired, please logout and try again"
        );
    }
    
    
    @Test
    public void testAddQuestionOk() {
        sv.createQuestion("type1", "q3");
        // This should not throw an exception
    }
    
    @Test
    public void testAddQuestionToNonExistingType() {
        expectException(()->
            sv.createQuestion("type3", "q3"),
            "The product type type3 does not exist"
        );
    }
    
    @Test
    public void cannotAddQuestionWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.createQuestion("type1", "q3"),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotAddQuestionWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.createQuestion("type1", "q3"),
            "This session has expired, please logout and try again"
        );
    }
    
    
    @Test
    public void testUpdateQuestionOk() {
        sv.editQuestion(1, "new statement");
        // This should not throw an exception
    }
    
    @Test
    public void testUpdateNonExistingQuestion() {
        expectException(()->
            sv.editQuestion(2, "new statement"),
            "The question with id 2 does not exist"
        );
    }
    
    @Test
    public void cannotUpdateQuestionWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.editQuestion(1, "new statement"),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotUpdateQuestionWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.editQuestion(1, "new statement"),
            "This session has expired, please logout and try again"
        );
    }
    
    
    @Test
    public void testDeleteQuestionOk() {
        sv.deleteQuestion(1);
        // This should not throw an exception
    }
    
    @Test
    public void testDeleteNonExistingQuestion() {
        expectException(()->
            sv.deleteQuestion(2),
            "The question with id 2 does not exist"
        );
    }
    
    @Test
    public void cannotDeleteQuestionWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.deleteQuestion(1),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotDeleteQuestionWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.deleteQuestion(1),
            "This session has expired, please logout and try again"
        );
    }
}
