package com.econnect.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import android.location.Location;

import com.econnect.API.HttpClient.StubHttpClient;
import com.econnect.API.ProfileService;
import com.econnect.API.ServiceFactory;

import org.junit.Before;
import org.junit.Test;

public class ProfileServiceTest {
    private ProfileService sv;
    
    @Before
    public void setUp() {
        sv = ServiceFactory.getInstance().getProfileService();
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
    //peta json
    public void testGetInfoLoggedUserOK() {
        ProfileService.User user = sv.getInfoLoggedUser();
        assertNotNull(user);
        assertEquals("user1", user.username);
        assertEquals(1, user.activeMedal);
        assertNull(user.home);
        assertEquals("user1@gmail.com", user.email);
        assertEquals(true, user.isPrivate);
        assertEquals(2, user.medals.length);
        assertEquals(1, user.medals[0].idmedal);
        assertEquals(2, user.medals[1].idmedal);
    }

    @Test
    public void testGetInfoOtherUserOK() {
        ProfileService.User user = sv.getInfoOtherUser(2);
        assertNotNull(user);
        assertEquals("user2", user.username);
        assertEquals(1234, user.activeMedal);
        assertNull(user.home);
        assertNull(user.email);
        assertNull(user.isPrivate);
        assertEquals(2, user.medals.length);
        assertEquals(1, user.medals[0].idmedal);
        assertEquals(2, user.medals[1].idmedal);
    }

    @Test
    public void testGetInfoOtherUserPrivate() {
        expectException(() ->
                sv.getInfoOtherUser(3),
                "The profile of this user is private"
        );
    }

    @Test
    public void tesUpdateUsernameOK() {
        sv.updateUsername("userTest");
        // This should not throw an exception
    }

    @Test
    public void tesUpdateUsernameExistent() {
        expectException(() ->
                sv.updateUsername("userExistent"),
                "This username has already been taken"
        );
    }

    @Test
    public void tesUpdatePasswordOK() {
        sv.updatePassword("123", "456");
        // This should not throw an exception
    }

    @Test
    public void tesUpdatePasswordAntigaIncorrecta() {
        expectException(() ->
                sv.updatePassword("---", "456"),
                "The old password is incorrect"
        );
    }

    @Test
    public void tesUpdateEmailOK() {
        sv.updateEmail("email2");
        // This should not throw an exception
    }

    @Test
    public void tesUpdateEmailExistent() {
        expectException(() ->
                sv.updateEmail("emailExistent"),
                "There is already an account with this email"
        );
    }

    @Test
    public void tesUpdateEmailInvalid() {
        expectException(() ->
                sv.updateEmail("emailInvalid"),
                "Please enter a valid email"
        );
    }

    @Test
    public void tesUpdateActiveMedalOK() {
        sv.updateActiveMedal(2);
        // This should not throw an exception
    }

    @Test
    public void tesUpdateActiveMedalInvalid() {
        expectException(() ->
                sv.updateActiveMedal(5),
                "This medal is incorrect"
        );
    }

    @Test
    public void tesUpdateAccountVisibilityOK() {
        sv.updateAccountVisibility(true);
        // This should not throw an exception
    }

    @Test
    public void tesDeleteAccountOK() {
        sv.deleteAccount();
        // This should not throw an exception
    }

    @Test
    //peta por la excepcion
    public void tesDeleteAccountIncorrectPassword() {
        ServiceTestHelper.clearToken();
        expectException(() ->
                sv.deleteAccount(),
                "User token not set"
        );
    }


    @Test
    public void cannotGetProductsWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
                        sv.deleteAccount(),
                "This session has expired, please logout and try again"
        );
    }
    

    @Test
    public void testGetHomeCoordinatesOk() {
        ProfileService.HomeCoords loc = sv.getHomeLocation();
        assertEquals(12.0, loc.latitude, 0);
        assertEquals(34.0, loc.longitude, 0);
        // This should not throw an exception
    }

    @Test
    public void testGetHomeCoordinatesNoHome() {
        ServiceTestHelper.setToken("userNoHome");
        ProfileService.HomeCoords loc = sv.getHomeLocation();
        assertNull(loc);
        // This should not throw an exception
    }

    @Test
    public void cannotGetHomeCoordinatesWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(() ->
                sv.getHomeLocation(),
                "User token not set"
        );
    }

    @Test
    public void cannotGetHomeCoordinatesWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
                sv.getHomeLocation(),
                "This session has expired, please logout and try again"
        );
    }

}
