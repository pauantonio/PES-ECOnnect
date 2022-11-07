package ECOnnect.API;

import ECOnnect.API.HttpClient.OkHttpAdapter;

public class ServiceFactory {
    // Singleton
    private static ServiceFactory _instance = null;
    private ServiceFactory() {}
    public static ServiceFactory getInstance() {
        if (_instance == null) {
            _instance = new ServiceFactory();
            // Use OkHttp library
            Service.injectHttpClient(new OkHttpAdapter());
            
            //Service.injectHttpClient(new ECOnnect.API.HttpClient.StubHttpClient());
        }
        return _instance;
    }
    
    // Admin Login
    private LoginService _adminLoginService = null;
    public LoginService getLoginService() {
        if (_adminLoginService == null) {
            _adminLoginService = new LoginService();
        }
        return _adminLoginService;
    }

    
    // Product Types
    private ProductTypesService _productTypesService = null;
    public ProductTypesService getProductTypesService() {
        if (_productTypesService == null) {
            _productTypesService = new ProductTypesService();
        }
        return _productTypesService;
    }
    
    // Products
    private ProductService _productService = null;
    public ProductService getProductService() {
        if (_productService == null) {
            _productService = new ProductService();
        }
        return _productService;
    }
    
    // Companies
    private CompanyService _companyService = null;
    public CompanyService getCompanyService() {
        if (_companyService == null) {
            _companyService = new CompanyService();
        }
        return _companyService;
    }
    
    // Forum
    private ForumService _forumService = null;
    public ForumService getForumService() {
        if (_forumService == null) {
            _forumService = new ForumService();
        }
        return _forumService;
    }
}
