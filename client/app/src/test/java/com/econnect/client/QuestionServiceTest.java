package com.econnect.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.econnect.API.ApiConstants;
import com.econnect.API.HttpClient.StubHttpClient;
import com.econnect.API.QuestionService;
import com.econnect.API.ServiceFactory;

import org.junit.Before;
import org.junit.Test;

public class QuestionServiceTest {
    QuestionService sv;

    @Before
    public void setUp() {
        sv = ServiceFactory.getInstance().getQuestionService();
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
    public void answerQuestionProductWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
                sv.answerQuestionProduct(1, 1, true),
                "User token not set"
        );
    }

    @Test
    public void answerQuestionProductOK() {
        ServiceTestHelper.setToken();
        sv.answerQuestionProduct(1, 1, true);
    }

    @Test
    public void answerQuestionProductFail() {
        ServiceTestHelper.setToken();
        expectException(() ->
                sv.answerQuestionProduct(2, 1, true),
                "The product with id 2 does not exist"
        );
    }

    @Test
    public void removeQuestionProductWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
                sv.removeQuestionProduct(2, 1),
                "User token not set"
        );
    }

    @Test
    public void removeQuestionProductOK() {
        ServiceTestHelper.setToken();
        sv.removeQuestionProduct(1, 1);
    }

    @Test
    public void removeQuestionProductFail() {
        ServiceTestHelper.setToken();
        expectException(() ->
                sv.removeQuestionProduct(2, 1),
                "The product with id 2 does not exist"
        );
    }

    @Test
    public void answerQuestionCompanyWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
                sv.answerQuestionCompany(2, 1, true),
                "User token not set"
        );
    }

    @Test
    public void answerQuestionCompanyOK() {
        ServiceTestHelper.setToken();
        sv.answerQuestionCompany(1, 1, true);
    }

    @Test
    public void answerQuestionCompanyFail() {
        ServiceTestHelper.setToken();
        expectException(() ->
                sv.answerQuestionCompany(2, 1, true),
                "The product with id 2 does not exist"
        );
    }

    @Test
    public void removeQuestionCompanyWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(()->
                sv.removeQuestionCompany(1, 1),
                "User token not set"
        );
    }

    @Test
    public void removeQuestionCompanyOK() {
        ServiceTestHelper.setToken();
        sv.removeQuestionCompany(1, 1);
    }

    @Test
    public void removeQuestionCompanyFail() {
        ServiceTestHelper.setToken();
        expectException(() ->
                sv.removeQuestionCompany(2, 1),
                "The product with id 2 does not exist"
        );
    }

}
