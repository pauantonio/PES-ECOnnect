package com.econnect.client;

import org.junit.*;

import com.econnect.API.*;
import com.econnect.API.ForumService.Post;
import com.econnect.API.ForumService.Tag;
import com.econnect.API.HttpClient.StubHttpClient;
import com.econnect.API.ImageUpload.ImageService;

import static org.junit.Assert.*;

public class ForumServiceTest {
    private ForumService sv;
    
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
        new ImageService();
        Post[] posts = sv.getPosts(4, null);
        assertNotNull(posts);
        assertEquals(3, posts.length);

        assertEquals(1, posts[0].postid);
        assertEquals("user1", posts[0].username);
        assertEquals(1, posts[0].userid);
        assertEquals(1, posts[0].medal);
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
        assertEquals(2, posts[1].medal);
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
        assertEquals(3, posts[2].medal);
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
        assertEquals(1, posts[0].medal);
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
            sv.getPosts(-1, ""),
            "The server responded with error code invalid value of n"
        );
        expectException(() ->
            sv.getPosts(0, ""),
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
            sv.getPosts(3, ""),
            "User token not set"
        );
    }
    
    @Test
    public void cannotGetPostsWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.getPosts(3, ""),
            "This session has expired, please logout and try again"
        );
    }
    
    
    @Test
    public void testGetTagsOk() {
        Tag[] tags = sv.getAllTags();
        assertNotNull(tags);
        assertEquals(3, tags.length);
        
        assertEquals("tag1", tags[0].tag);
        assertEquals(15, tags[0].count);
        
        assertEquals("tag2", tags[1].tag);
        assertEquals(2, tags[1].count);
        
        assertEquals("abc", tags[2].tag);
        assertEquals(1, tags[2].count);
    }
    
    @Test
    public void testGetTagsWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.getAllTags(),
            "User token not set"
        );
    }
    
    @Test
    public void testGetTagsWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.getAllTags(),
            "This session has expired, please logout and try again"
        );
    }
    
    
    @Test
    public void testDeletePostOk() {
        sv.deletePost(1);
        // This should not throw an exception
    }
    
    @Test
    public void testDeletePostNotExists() {
        expectException(()->
            sv.deletePost(3),
            "The post with id 3 does not exist"
        );
    }
    
    @Test
    public void testDeletePostFromOtherUser() {
        expectException(()->
            sv.deletePost(2),
            "You don't have permission to delete this post"
        );
    }
    
    @Test
    public void cannotDeletePostWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.deletePost(1),
            "User token not set"
        );
    }
    
    @Test
    public void cannotDeletePostWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.deletePost(1),
            "This session has expired, please logout and try again"
        );
    }
    
    
    @Test
    public void testLikePostOk() {
        sv.likePost(1, true, false);
        // This should not throw an exception
    }
    
    @Test
    public void testDislikePostOk() {
        sv.likePost(1, false, false);
        // This should not throw an exception
    }
    
    @Test
    public void testUnlikePostOk() {
        sv.likePost(1, true, true);
        // This should not throw an exception
    }
    
    @Test
    public void testUndislikePostOk() {
        sv.likePost(1, false, true);
        // This should not throw an exception
    }
    
    @Test
    public void testLikePostNotExists() {
        expectException(()->
            sv.likePost(3, true, false),
            "The post with id 3 does not exist"
        );
    }
    
    @Test
    public void testLikePostFromOtherUser() {
        sv.likePost(2, true, false);
        // This should not throw an exception
    }
    
    @Test
    public void cannotLikePostWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
            sv.likePost(1, true, false),
            "User token not set"
        );
    }
    
    @Test
    public void cannotLikePostWithWrongToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(()->
            sv.likePost(1, true, false),
            "This session has expired, please logout and try again"
        );
    }
}
