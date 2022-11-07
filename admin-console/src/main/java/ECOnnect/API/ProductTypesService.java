package ECOnnect.API;

import java.util.TreeMap;

import ECOnnect.API.Exceptions.ApiException;

public class ProductTypesService extends Service {
    
    // Only allow instantiating from ServiceFactory
    ProductTypesService() {}
    
    public static class ProductType {
        public static class Question {
            public final int questionid;
            public String statement;

            public Question(int questionid, String statement) {
                this.questionid = questionid;
                this.statement = statement;
            }
        }

        // Important: The name of these attributes must match the ones in the returned JSON
        // Gson will initialize these fields to the received values
        public String name;
        public final Question[] questions;

        public ProductType(String name, Question[] questions) {
            this.name = name;
            this.questions = questions;
        }
    }
    
    private static class QuestionsStruct {
        @SuppressWarnings("unused")
        public final String[] questions;
        public QuestionsStruct(String[] questions) {
            this.questions = questions;
        }
    }
    
    // Get all product types
    public ProductType[] getProductTypes() {
        // Call API
        super.needsToken = true;
        JsonResult result = get(ApiConstants.TYPES_PATH, null);
        
        // Parse result
        ProductType[] productTypes = result.getArray(ApiConstants.RET_RESULT, ProductType[].class);
        assertResultNotNull(productTypes, result);
        
        // Trim spaces in questions
        for (ProductType productType : productTypes) {
            for (int i = 0; i < productType.questions.length; i++) {
                productType.questions[i].statement = productType.questions[i].statement.trim();
            }
        }
        
        return productTypes;
    }
    
    // Create a new product type
    public void createProductType(String name, String[] questions) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.PRODUCT_TYPES_NAME, name);
        
        JsonResult result = null;
        try {
            // Call API
            super.needsToken = true;
            result = post(ApiConstants.TYPES_PATH, params, new QuestionsStruct(questions));
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_TYPE_EXISTS:
                    throw new RuntimeException("There is already a product type with this name");
                default:
                    throw e;
            }
        }
        
        expectOkStatus(result);
    }
    
    // Update an existing product type
    public void renameType(String oldName, String newName) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.PRODUCT_TYPES_NEW_NAME, newName);
        params.put(ApiConstants.PRODUCT_TYPES_OLD_NAME, oldName);
        
        // Call API
        JsonResult result = null;
        try {
            super.needsToken = true;
            result = put(ApiConstants.TYPES_PATH, params, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_TYPE_NOT_EXISTS:
                    throw new RuntimeException("The product type " + oldName + " does not exist");
                case ApiConstants.ERROR_TYPE_EXISTS:
                    throw new RuntimeException("There is already a product type called " + newName);
                default:
                    throw e;
            }
        }
        
        expectOkStatus(result);
    }
    
    // Delete an existing product type
    public void deleteProductType(String name) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.PRODUCT_TYPES_NAME, name);
        
        JsonResult result = null;
        try {
            // Call API
            super.needsToken = true;
            result = delete(ApiConstants.TYPES_PATH, params);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_TYPE_NOT_EXISTS:
                    throw new RuntimeException("The product type " + name + " does not exist");
                default:
                    throw e;
            }
        }
        
        expectOkStatus(result);
    }
    
    
    // Create a question
    public void createQuestion(String type, String statement) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.QUESTION_TYPE, type);
        params.put(ApiConstants.QUESTION_STATEMENT, statement);
        
        JsonResult result = null;
        try {
            // Call API
            super.needsToken = true;
            result = post(ApiConstants.QUESTIONS_PATH, params, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_TYPE_NOT_EXISTS:
                    throw new RuntimeException("The product type " + type + " does not exist");
                default:
                    throw e;
            }
        }
        
        expectOkStatus(result);
    }
    
    // Edit a question
    public void editQuestion(int questionId, String newStatement) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.QUESTION_NEW_STATEMENT, newStatement);
        
        // Call API
        JsonResult result = null;
        try {
            super.needsToken = true;
            result = put(ApiConstants.QUESTIONS_PATH + "/" + questionId, params, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_QUESTION_NOT_EXISTS:
                    throw new RuntimeException("The question with id " + questionId + " does not exist");
                default:
                    throw e;
            }
        }
        
        expectOkStatus(result);
    }
    
    // Delete a question
    public void deleteQuestion(int questionId) {
        // Call API
        JsonResult result = null;
        try {
            super.needsToken = true;
            result = delete(ApiConstants.QUESTIONS_PATH + "/" + questionId, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_QUESTION_NOT_EXISTS:
                    throw new RuntimeException("The question with id " + questionId + " does not exist");
                default:
                    throw e;
            }
        }
        
        expectOkStatus(result);
    }
}
