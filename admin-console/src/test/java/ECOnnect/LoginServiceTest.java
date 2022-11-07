package ECOnnect;

import org.junit.*;

import ECOnnect.API.*;

import static org.junit.Assert.*;

public class LoginServiceTest {
    LoginService sv;
    
    @Before
    public void setUp() {
        sv = ServiceFactory.getInstance().getLoginService();
        ServiceTestHelper.injectHttpClient(new StubHttpClient());
        ServiceTestHelper.clearToken();
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
    public void testLoginOk() {
        sv.login("okEmail", "okPassword");
        // This should not throw an exception
    }
    
    @Test
    public void cannotLoginTwice() {
        sv.login("okEmail", "okPassword");
        expectException(() ->
            sv.login("okEmail", "okPassword"),
            "Token already set"
        );
    }
    
    @Test
    public void testLoginIncorrectEmail() {
        expectException(() ->
            sv.login("wrongEmail", "wrongPassword"),
            "No account found for this email"
        );
    }
    
    @Test
    public void testLoginIncorrectPassword() {
        expectException(() ->
            sv.login("okEmail", "wrongPassword"),
            "Incorrect password for this email"
        );
    }
    
    @Test
    public void testLoginNoAdmin() {
        expectException(() ->
            sv.login("okEmailNoAdmin", "okPassword"),
            "This user is not an admin"
        );
    }
    
    
    @Test
    public void testLoginNulls() {
        expectException(() ->
            sv.login(null, "abc"),
            "Parameter email cannot be null"
        );
        
        expectException(() ->
            sv.login("okEmail", null),
            "Parameter password cannot be null"
        );
    }
    
    
    @Test
    public void testLogoutOk() {
        ServiceTestHelper.setToken();
        sv.logout();
        // This should not throw an exception
    }
    
    @Test
    public void testLogoutBadToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
            sv.logout(),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void testLoginAndLogout() {
        sv.login("okEmail", "okPassword");
        sv.logout();
        // This should not throw an exception
    }
    
    @Test
    public void cannotLogoutTwice() {
        ServiceTestHelper.setToken();
        sv.logout();
        expectException(() ->
            sv.logout(),
            "Admin token not set"
        );
    }
}
