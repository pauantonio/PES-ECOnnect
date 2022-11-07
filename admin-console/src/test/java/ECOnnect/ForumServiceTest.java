package ECOnnect;

import org.junit.*;

import ECOnnect.API.*;
import ECOnnect.API.ForumService.Post;

import static org.junit.Assert.*;

public class ForumServiceTest {
    ForumService sv;
    
    @Before
    public void setUp() {
        sv = ServiceFactory.getInstance().getForumService();
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
    public void testGetPostsOk() {
        Post[] posts = sv.getPosts(4, null);
        assertNotNull(posts);
        assertEquals(3, posts.length);
        
        assertEquals(1, posts[0].postid);
        assertEquals("user1", posts[0].username);
        assertEquals(1, posts[0].userid);
        assertEquals("m1", posts[0].medal);
        assertEquals("#tag1 text1", posts[0].text);
        assertEquals("https://wallpapercave.com/wp/wp4676582.jpg", posts[0].imageurl);
        assertEquals(1, posts[0].likes);
        assertEquals(2, posts[0].dislikes);
        assertEquals(1, posts[0].useroption);
        assertEquals(1649663866, posts[0].timestamp, 0);
        assertTrue(posts[0].ownpost);
        assertFalse(posts[0].authorbanned);
        
        assertEquals(2, posts[1].postid);
        assertEquals("user2", posts[1].username);
        assertEquals(2, posts[1].userid);
        assertEquals("m2", posts[1].medal);
        assertEquals("text2 #another . #tag2", posts[1].text);
        assertEquals("image2", posts[1].imageurl);
        assertEquals(3, posts[1].likes);
        assertEquals(4, posts[1].dislikes);
        assertEquals(0, posts[1].useroption);
        assertEquals(1649663810, posts[1].timestamp, 0);
        assertFalse(posts[1].ownpost);
        assertTrue(posts[1].authorbanned);
        
        assertEquals(3, posts[2].postid);
        assertEquals("Another User", posts[2].username);
        assertEquals(3, posts[2].userid);
        assertEquals("m3", posts[2].medal);
        assertEquals("Post without tags.", posts[2].text);
        assertEquals("https://images.unsplash.com/photo-1559583985-c80d8ad9b29f", posts[2].imageurl);
        assertEquals(1234, posts[2].likes);
        assertEquals(1234, posts[2].dislikes);
        assertEquals(2, posts[2].useroption);
        assertEquals(1649836904, posts[2].timestamp, 0);
        assertTrue(posts[2].ownpost);
        assertFalse(posts[2].authorbanned);
    }
    
    @Test
    public void testGetPostsWithTag() {
        Post[] posts = sv.getPosts(3, "tag1");
        assertNotNull(posts);
        assertEquals(1, posts.length);
        
        assertEquals(1, posts[0].postid);
        assertEquals("user1", posts[0].username);
        assertEquals(1, posts[0].userid);
        assertEquals("m1", posts[0].medal);
        assertEquals("#tag1 text1", posts[0].text);
        assertEquals("https://wallpapercave.com/wp/wp4676582.jpg", posts[0].imageurl);
        assertEquals(1, posts[0].likes);
        assertEquals(2, posts[0].dislikes);
        assertEquals(1, posts[0].useroption);
        assertEquals(1649663866, posts[0].timestamp, 0);
    }
    
    @Test
    public void testGetPostsInvalidAmount() {
        expectException(() -> 
            sv.getPosts(-1, null),
            "The server responded with error code invalid value of n"
        );
        expectException(() ->
            sv.getPosts(0, null),
            "The server responded with error code invalid value of n"
        );
    }
    
    @Test
    public void testGetPostsTagNotExists() {
        Post[] posts = sv.getPosts(3, "badTag");
        assertNotNull(posts);
        assertEquals(0, posts.length);
    }
    
    @Test
    public void cannotGetPostsWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.getPosts(3, null),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotGetPostsWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.getPosts(3, null),
            "This session has expired, please logout and try again"
        );
    }
    
    
    
    @Test
    public void testBanUserOk() {
        sv.banUser(1, true);
        sv.banUser(2, true);
        sv.banUser(1, false);
        sv.banUser(2, false);
        // This should not throw an exception
    }
    
    @Test
    public void testBanUserNotExists() {
        expectException(()->
            sv.banUser(3, false),
            "The user with id 3 does not exist"
        );
    }
    
    @Test
    public void cannotBanUserWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.banUser(1, false),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotBanUserWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.banUser(1, false),
            "This session has expired, please logout and try again"
        );
    }
    
    @Test
    public void testGetIsUserBannedOk() {
        assertFalse(sv.isBanned(1));
        assertTrue(sv.isBanned(2));
    }
    
    @Test
    public void testGetIsUserBannedNotExists() {
        expectException(()->
            sv.isBanned(3),
            "The user with id 3 does not exist"
        );
    }
    
    @Test
    public void cannotGetIsUserBannedWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.isBanned(1),
            "Admin token not set"
        );
    }
    
    @Test
    public void cannotGetIsUserBannedWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.isBanned(1),
            "This session has expired, please logout and try again"
        );
    }
    
}
