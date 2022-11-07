package com.econnect.API;

import com.econnect.API.HttpClient.OkHttpAdapter;
import com.econnect.API.HttpClient.StubHttpClient;

public class ServiceFactory {
    // Singleton
    private static ServiceFactory instance = null;
    private ServiceFactory() {}
    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
            // Use OkHttp library
            Service.injectHttpClient(new OkHttpAdapter());
            
            // TODO: Remove this once the backend works
            //Service.injectHttpClient(new StubHttpClient());
        }
        return instance;
    }
    
    
    // Login
    private static LoginService loginService = null;
    public LoginService getLoginService() {
        if (loginService == null) {
            loginService = new LoginService();
        }
        return loginService;
    }

    // Register
    private static RegisterService registerService = null;
    public RegisterService getRegisterService() {
        if (registerService == null) {
            registerService = new RegisterService();
        }
        return registerService;
    }

    // Product Types
    private static ProductTypesService _productTypesService = null;
    public ProductTypesService getProductTypesService() {
        if (_productTypesService == null) {
            _productTypesService = new ProductTypesService();
        }
        return _productTypesService;
    }
    
    // Products
    private static ProductService _productService = null;
    public ProductService getProductService() {
        if (_productService == null) {
            _productService = new ProductService();
        }
        return _productService;
    }
    
    // Companies
    private static CompanyService _companyService = null;
    public CompanyService getCompanyService() {
        if (_companyService == null) {
            _companyService = new CompanyService();
        }
        return _companyService;
    }

    private static ReviewService _reviewService = null;
    public ReviewService getReviewService() {
        if (_reviewService == null) {
            _reviewService = new ReviewService();
        }
        return _reviewService;
    }

    private static QuestionService _questionService = null;
    public QuestionService getQuestionService() {
        if(_questionService == null) {
            _questionService = new QuestionService();
        }
        return _questionService;
    }

    private static ForumService _forumService = null;
    public ForumService getForumService() {
        if(_forumService == null) {
            _forumService = new ForumService();
        }
        return _forumService;
    }

    private static ProfileService _profileService = null;
    public ProfileService getProfileService() {
        if(_profileService == null) {
            _profileService = new ProfileService();
        }
        return _profileService;
    }

    private static HomeService _homeService = null;
    public HomeService getHomeService() {
        if(_homeService == null) {
            _homeService = new HomeService();
        }
        return _homeService;
    }

}
