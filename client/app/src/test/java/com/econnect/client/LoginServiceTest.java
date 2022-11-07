package com.econnect.client;

import org.junit.*;

import com.econnect.API.*;
import com.econnect.API.HttpClient.StubHttpClient;
import com.econnect.Utilities.Translate;

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
        sv.login("okEmail", "okPassword", null);
        // This should not throw an exception
    }
    
    @Test
    public void cannotLoginTwice() {
        sv.login("okEmail", "okPassword", null);
        expectException(() ->
            sv.login("okEmail", "okPassword", null),
            "Token already set"
        );
    }
    
    @Test
    public void testLoginIncorrectEmail() {
        expectException(() ->
            sv.login("wrongEmail", "wrongPassword", null),
            "No account found for this email"
        );
    }
    
    @Test
    public void testLoginIncorrectPassword() {
        expectException(() ->
            sv.login("okEmail", "wrongPassword", null),
            "Incorrect password for this email"
        );
    }
    
    
    @Test
    public void testLoginNulls() {
        expectException(() ->
            sv.login(null, "abc", null),
            "Parameter email cannot be null"
        );
        
        expectException(() ->
            sv.login("okEmail", null, null),
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
        sv.login("okEmail", "okPassword", null);
        sv.logout();
        // This should not throw an exception
    }
    
    @Test
    public void cannotLogoutTwice() {
        ServiceTestHelper.setToken();
        sv.logout();
        expectException(() ->
            sv.logout(),
            "User token not set"
        );
    }
}
