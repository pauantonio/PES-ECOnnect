package com.econnect.client;

import org.junit.*;

import com.econnect.API.*;
import com.econnect.API.CompanyService.Company;
import com.econnect.API.CompanyService.CompanyDetails;
import com.econnect.API.HttpClient.StubHttpClient;

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
        assertEquals("company1", companies[0].getName());
        assertEquals(1.0f, companies[0].getAvgRating(), 0.0f);
        assertEquals("https://wallpapercave.com/wp/wp4676582.jpg", companies[0].imageurl);
        assertEquals(1.0, companies[0].lat, 0.0);
        assertEquals(1.0, companies[0].lon, 0.0);
        
        assertEquals(2, companies[1].id);
        assertEquals("company2", companies[1].getName());
        assertEquals(2.0f, companies[1].getAvgRating(), 0.0f);
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
            "User token not set"
        );
    }


    @Test
    public void getCompanyDetailsOk() {
        CompanyDetails companyDetails = sv.getCompanyDetails(1);

        assertNotNull(companyDetails);

        assertEquals("https://company.com/img.png", companyDetails.imageURL);
        assertEquals(12, companyDetails.latitude, 0);
        assertEquals(34, companyDetails.longitude, 0);
        assertEquals("test", companyDetails.name);

        assertEquals(2, companyDetails.questions.length);
        assertEquals(1, companyDetails.questions[0].num_no);
        assertEquals(2, companyDetails.questions[0].num_yes);
        assertEquals("bon servei?", companyDetails.questions[0].text);
        assertEquals(3, companyDetails.questions[1].num_no);
        assertEquals(4, companyDetails.questions[1].num_yes);
        assertEquals("q2", companyDetails.questions[1].text);

        assertEquals(6, companyDetails.ratings.length);
        assertEquals(1, companyDetails.ratings[0]);
        assertEquals(2, companyDetails.ratings[1]);
        assertEquals(6, companyDetails.ratings[5]);
    }
    
    @Test
    public void testGetCompanyDetailsNotExists() {
        expectException(() ->
            sv.getCompanyDetails(2),
            "The company with id 2 does not exist"
        );
    }
    
    @Test
    public void cannotGetCompanyDetailsWithInvalidToken() {
        ServiceTestHelper.setToken("badToken");
        expectException(() ->
            sv.getCompanyDetails(1),
            "This session has expired, please logout and try again"
        );
    }

    @Test
    public void cannotGetCompanyDetailsWithoutToken() {
        ServiceTestHelper.clearToken();
        expectException(() ->
            sv.getCompanyDetails(1),
            "User token not set"
        );
    }
}
