package ECOnnect.UI.Company;

import ECOnnect.API.CompanyService;
import ECOnnect.API.ServiceFactory;
import ECOnnect.API.CompanyService.Company;
import ECOnnect.API.ProductTypesService.ProductType.Question;

public class CompanyModel {
    // Get all the companies
    Company[] getCompanies() {
        // Get products from API
        CompanyService service = ServiceFactory.getInstance().getCompanyService();
        // No need to store companies in model
        return service.getCompanies();
    }
    
    public void addCompany(String name, String imageUrl, String latitude, String longitude) {
        // Trim whitespace
        name = name.trim();
        imageUrl = imageUrl.trim();
        
        checkParams(name, imageUrl, latitude, longitude);
        
        // Add product type to API
        CompanyService service = ServiceFactory.getInstance().getCompanyService();
        service.createCompany(name, imageUrl, Double.parseDouble(latitude), Double.parseDouble(longitude));
    }
    
    public void updateCompany(int id, String name, String imageUrl, String latitude, String longitude) {
        // Trim whitespace
        name = name.trim();
        imageUrl = imageUrl.trim();
        
        checkParams(name, imageUrl, latitude, longitude);
        
        // Add product type to API
        CompanyService service = ServiceFactory.getInstance().getCompanyService();
        service.updateCompany(id, name, imageUrl, Double.parseDouble(latitude), Double.parseDouble(longitude));
    }
    
    private void checkParams(String name, String imageUrl, String latitude, String longitude) {
        
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (imageUrl.isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be empty");
        }
        if (latitude.isEmpty()) {
            throw new IllegalArgumentException("Latitude cannot be empty");
        }
        if (longitude.isEmpty()) {
            throw new IllegalArgumentException("Longitude cannot be empty");
        }
        
        double lat;
        double lon;
        try {
            lat = Double.parseDouble(latitude);
            lon = Double.parseDouble(longitude);
        }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Latitude and longitude must be valid numbers");
        }
        
        // Check for NaN and infinities
        if (Double.isNaN(lat) || Double.isInfinite(lat)) {
            throw new IllegalArgumentException("Latitude must be a number");
        }
        if (Double.isNaN(lon) || Double.isInfinite(lon)) {
            throw new IllegalArgumentException("Longitude must be a number");
        }
        
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }
    
    public Question[] getQuestions() {
        // Get questions from API
        CompanyService service = ServiceFactory.getInstance().getCompanyService();
        // No need to store questions in model
        return service.getQuestions();
    }
    
    public void createQuestion() {
        CompanyService service = ServiceFactory.getInstance().getCompanyService();
        service.createQuestion("");
    }

    public void removeCompany(int id) {
        CompanyService service = ServiceFactory.getInstance().getCompanyService();
        service.deleteCompany(id);
    }
}
