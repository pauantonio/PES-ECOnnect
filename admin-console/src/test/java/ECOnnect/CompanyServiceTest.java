package ECOnnect;

import org.junit.*;

import ECOnnect.API.*;
import ECOnnect.API.CompanyService.Company;
import ECOnnect.API.ProductTypesService.ProductType.Question;

import static org.junit.Assert.*;

public class CompanyServiceTest {
    private CompanyService sv;
    
    @Before
    public void setUp() {
        sv = ServiceFactory.getInstance().getCompanyService();
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
    public void testGetCompanyOk() {
        Company[] companies = sv.getCompanies();
        // This should not throw an exception
        
        assertNotNull(companies);
        assertEquals(2, companies.length);
        
        assertEquals(1, companies[0].id);
        assertEquals("company1", companies[0].name);
        assertEquals(1.0f, companies[0].avgrating, 0.0f);
        assertEquals("http://www.company1.com/image.png", companies[0].imageurl);
        assertEquals(1.0, companies[0].lat, 0.0);
        assertEquals(1.0, companies[0].lon, 0.0);
        
        assertEquals(2, companies[1].id);
        assertEquals("company2", companies[1].name);
        assertEquals(2.0f, companies[1].avgrating, 0.0f);
        assertEquals("http://www.company2.com/image.png", companies[1].imageurl);
        assertEquals(2.0, companies[1].lat, 0.0);
        assertEquals(2.0, companies[1].lon, 0.0);
    }
    
    @Test
    public void cannotGetCompaniesWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
            sv.getCompanies(),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotGetCompaniesWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(() ->
            sv.getCompanies(),
            "Admin token not set"
        );
    }
    
    
    @Test
    public void testCreateCompanyOk() {        
        sv.createCompany("newCompany", "http://www.newcompany.com/image.png", 1.0, 1.0);
        // This should not throw an exception
    }
    
    @Test
    public void cannotCreateExistingCompany() {
        expectException(() ->
            sv.createCompany("company1", "http://www.newcompany.com/image.png", 1.0, 1.0),
            "The company company1 already exists"
        );
    }
    
    @Test
    public void cannotCreateCompanyWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
            sv.createCompany("newCompany", "http://www.newcompany.com/image.png", 1.0, 1.0),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotCreateCompanyWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(() ->
            sv.createCompany("newCompany", "http://www.newcompany.com/image.png", 1.0, 1.0),
            "Admin token not set"
        );
    }
    
    @Test
    public void testCreateCompanyNulls() {
        expectException(() ->
            sv.createCompany(null, "http://www.newcompany.com/image.png", 1.0, 1.0),
            "Parameter name cannot be null"
        );
        
        expectException(() ->
            sv.createCompany("newCompany", null, 1.0, 1.0),
            "Parameter imageURL cannot be null"
        );
    }
    
    
    @Test
    public void testGetCompanyQuestionsOk() {
        Question[] questions = sv.getQuestions();
        // This should not throw an exception
        
        assertNotNull(questions);
        assertEquals(3, questions.length);
        
        assertEquals("q1", questions[0].statement);
        assertEquals("q2", questions[1].statement);
        assertEquals("q3", questions[2].statement);
    }
    
    @Test
    public void cannotGetCompanyQuestionsWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
            sv.getQuestions(),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotGetCompanyQuestionsWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(() ->
            sv.getQuestions(),
            "Admin token not set"
        );
    }
    
    
    @Test
    public void testCreateCompanyQuestionOk() {
        sv.createQuestion("newQuestion");
        // This should not throw an exception
    }
    
    @Test
    public void testCreateCompanyQuestionStubValidation() {
        expectException(() ->
            sv.createQuestion("invalidQuestion"),
            "The server responded with error code invalid question"
        );
    }
    
    @Test
    public void cannotCreateCompanyQuestionWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
            sv.createQuestion("newQuestion"),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotCreateCompanyQuestionWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(() ->
            sv.createQuestion("newQuestion"),
            "Admin token not set"
        );
    }
    
    
    @Test
    public void testUpdateCompanyOk() {
        sv.updateCompany(1, "newName", "http://www.newcompany.com/image.png", 1.0, 1.0);
        // This should not throw an exception
    }
    
    @Test
    public void testUpdateCompanyNotExists() {
        expectException(() ->
            sv.updateCompany(2, "newName", "http://www.newcompany.com/image.png", 1.0, 1.0),
            "The company with id 2 does not exist"
        );
    }
    
    @Test
    public void testUpdateCompanyAlreadyExists() {
        expectException(()-> 
            sv.updateCompany(1, "existingCompany", "http://www.newcompany.com/image.png", 1.0, 1.0),
            "There is already a company with name existingCompany"
        );
    }
    
    @Test
    public void cannotUpdateCompanyWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
            sv.updateCompany(1, "newName", "http://www.newcompany.com/image.png", 1.0, 1.0),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotUpdateCompanyWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(() ->
            sv.updateCompany(1, "newName", "http://www.newcompany.com/image.png", 1.0, 1.0),
            "Admin token not set"
        );
    }
    
    
    @Test
    public void testDeleteCompanyOk() {
        sv.deleteCompany(1);
        // This should not throw an exception
    }
    
    @Test
    public void testDeleteCompanyNotExists() {
        expectException(() ->
            sv.deleteCompany(2),
            "The company with id 2 does not exist"
        );
    }
    
    @Test
    public void cannotDeleteCompanyWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
            sv.deleteCompany(1),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void cannotDeleteCompanyWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(() ->
            sv.deleteCompany(1),
            "Admin token not set"
        );
    }
}
