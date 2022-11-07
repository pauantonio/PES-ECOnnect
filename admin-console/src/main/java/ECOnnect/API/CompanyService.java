package ECOnnect.API;

import java.util.TreeMap;

import ECOnnect.API.Exceptions.ApiException;
import ECOnnect.API.ProductTypesService.ProductType.Question;

public class CompanyService extends Service {
    
    // Only allow instantiating from ServiceFactory
    CompanyService() {}
    
    public class Company {
        // Important: The name of these attributes must match the ones in the returned JSON
        public final int id;
        public final String name;
        public final float avgrating;
        public final String imageurl;
        public final double lat;
        public final double lon;
        
        public Company(int id, String name, float avgRating, String imageURL, double lat, double lon) {
            this.id = id;
            this.name = name;
            this.avgrating = avgRating;
            this.imageurl = imageURL;
            this.lat = lat;
            this.lon = lon;
        }
    }
    
    // Get all companies
    public Company[] getCompanies() {
        
        // Call API
        super.needsToken = true;
        JsonResult result = get(ApiConstants.COMPANIES_PATH, null);
        
        // Parse result
        Company[] companies = result.getArray(ApiConstants.RET_RESULT, Company[].class);
        assertResultNotNull(companies, result);
        return companies;
    }
    
    // Get questions for the company type
    public Question[] getQuestions() {
        
        // Call API
        super.needsToken = true;
        JsonResult result = get(ApiConstants.COMPANY_QUESTIONS_PATH, null);
        
        // Parse result
        Question[] questions = result.getArray(ApiConstants.RET_RESULT, Question[].class);
        assertResultNotNull(questions, result);
        
        // Trim spaces in questions
        for (int i = 0; i < questions.length; i++) {
            questions[i].statement = questions[i].statement.trim();
        }
        
        return questions;
    }
    
    public void createQuestion(String statement) {
            
            // Add parameters
            TreeMap<String, String> params = new TreeMap<>();
            params.put(ApiConstants.QUESTION_STATEMENT, statement);
            
            // Call API
            super.needsToken = true;
            JsonResult result = post(ApiConstants.COMPANY_QUESTIONS_PATH, params, null);
            expectOkStatus(result);
    }
    
    // Create a new company
    public void createCompany(String name, String imageURL, double lat, double lon) {
        try {
            // Call API
            updateCompanyImpl(name, imageURL, lat, lon, ApiConstants.COMPANIES_PATH);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_COMPANY_EXISTS:
                    throw new RuntimeException("The company " + name + " already exists");
                default:
                    throw e;
            }
        }
    }
    
    // Update an existing company
    public void updateCompany(int id, String name, String imageURL, double lat, double lon) {
        try {
            // Call API
            updateCompanyImpl(name, imageURL, lat, lon, ApiConstants.COMPANIES_PATH + "/" + id);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_COMPANY_NOT_EXISTS:
                    throw new RuntimeException("The company with id " + id + " does not exist");
                case ApiConstants.ERROR_COMPANY_EXISTS:
                    throw new RuntimeException("There is already a company with name " + name);
                default:
                    throw e;
            }
        }
    }

    private void updateCompanyImpl(String name, String imageURL, double lat, double lon, String path) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.COMPANY_NAME, name);
        params.put(ApiConstants.COMPANY_IMAGE_URL, imageURL);
        params.put(ApiConstants.COMPANY_LOCATION_LAT, String.valueOf(lat));
        params.put(ApiConstants.COMPANY_LOCATION_LON, String.valueOf(lon));
        
        // Call API
        super.needsToken = true;
        JsonResult result = post(path, params, null);
        expectOkStatus(result);
    }
    
    // Delete a company
    public void deleteCompany(int id) {
        JsonResult result;
        try {
            // Call API
            super.needsToken = true;
            result = delete(ApiConstants.COMPANIES_PATH + "/" + id, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_COMPANY_NOT_EXISTS:
                    throw new RuntimeException("The company with id " + id + " does not exist");
                default:
                    throw e;
            }
        }
        
        expectOkStatus(result);
    }
}
