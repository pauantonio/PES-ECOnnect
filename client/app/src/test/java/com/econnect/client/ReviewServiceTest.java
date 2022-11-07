package com.econnect.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.econnect.API.HttpClient.StubHttpClient;
import com.econnect.API.ReviewService;
import com.econnect.API.ServiceFactory;

import org.junit.Before;
import org.junit.Test;

public class ReviewServiceTest {
    ReviewService sv;

    @Before
    public void setUp() {
        sv = ServiceFactory.getInstance().getReviewService();
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
    public void testReviewOk() {
        ServiceTestHelper.setToken();
        sv.reviewProduct(1, 3);
        // This should not throw an exception
    }

    @Test
    public void invalidProductReview() {
        ServiceTestHelper.setToken();
        expectException(() ->
                        sv.reviewProduct(2, 3),
                "The product with id 2 does not exist"
        );
    }

}
