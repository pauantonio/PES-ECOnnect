package com.econnect.client;

import org.junit.*;

import com.econnect.API.*;
import com.econnect.API.HttpClient.StubHttpClient;

import static org.junit.Assert.*;

public class RegisterServiceTest {
    private RegisterService sv;
    
    @Before
    public void setUp() {
        sv = ServiceFactory.getInstance().getRegisterService();
        ServiceTestHelper.injectHttpClient(new StubHttpClient());
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
    public void testRegisterOk() {
        // TODO
    }
}
