package com.econnect.API;

public class ProductTypesService extends Service {
    
    // Only allow instantiating from ServiceFactory
    ProductTypesService() {}
    
    public static class ProductType {
        public static class Question {
            public final int questionid;
            public final String statement;

            public Question(int questionid, String statement) {
                this.questionid = questionid;
                this.statement = statement;
            }
        }

        // Important: The name of these attributes must match the ones in the returned JSON
        // Gson will initialize these fields to the received values
        public final String name;
        public String translatedName;
        public final Question[] questions;

        public ProductType(String name, Question[] questions) {
            this.name = name;
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
        
        return productTypes;
    }
}
