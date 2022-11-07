package com.econnect.API;

import android.graphics.Bitmap;

import java.util.TreeMap;

import com.econnect.API.Exceptions.ApiException;
import com.econnect.Utilities.BitmapLoader;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class ProductService extends Service {
    
    // Only allow instantiating from ServiceFactory
    ProductService() {}
    
    public static class Product implements IAbstractProduct {
        // Important: The name of these attributes must match the ones in the returned JSON
        // Gson will initialize these fields to the received values
        public final int id;
        public final String name;
        public final float avgrating;
        public final String manufacturer;
        public final String imageurl;
        public final String type;
        private Bitmap imageBitmap = null;

        public Product(int id, String name, float avgRating, String manufacturer, String imageURL, String type) {
            this.id = id;
            this.name = name;
            this.avgrating = avgRating;
            this.manufacturer = manufacturer;
            this.imageurl = imageURL;
            this.type = type;
        }

        @Override
        public String getName() {
            return name;
        }
        @Override
        public String getSecondaryText() {
            return manufacturer;
        }
        @Override
        public float getAvgRating() {
            return avgrating;
        }
        @Override
        public Bitmap getImage(int height) {
            if (imageBitmap == null)
                imageBitmap = BitmapLoader.fromURLResizeHeight(imageurl, height);
            return imageBitmap;
        }
    }

    public static class ProductDetails {

        public static class Question {
            public final int questionid;
            public int num_no;
            public int num_yes;
            public final String text;
            public String translatedText = null;
            public String user_answer;
            public Question(int questionid, int num_no, int num_yes, String text, String user_answer) {
                this.questionid = questionid;
                this.num_no = num_no;
                this.num_yes = num_yes;
                this.text = text;
                this.user_answer = user_answer;
            }
        }

        // Important: The name of these attributes must match the ones in the returned JSON
        // Gson will initialize these fields to the received values
        public final String imageURL;
        public final String manufacturer;
        public final String name;
        public final Question[] questions;
        public final int[] ratings;
        public int userRate;
        public ProductDetails(String imageURL, String manufacturer, String name, Question[] questions, int[] ratings, int userRate) {
            this.imageURL = imageURL;
            this.manufacturer = manufacturer;
            this.name = name;
            this.questions = questions;
            this.ratings = ratings;
            this.userRate= userRate;
        }

        public Question getQuestion(int id) {
            for (Question q : questions) {
                if (q.questionid == id)
                    return q;
            }
            return null;
        }
    }



    // Get product of specific type (or all products if type is null)
    public Product[] getProducts(String type) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        // Empty string means all products
        if (type == null) type = "";
        params.put(ApiConstants.PRODUCT_TYPE, type);
        
        
        JsonResult result;
        try {
            // Call API
            super.needsToken = true;
            result = get(ApiConstants.PRODUCTS_PATH, params);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_TYPE_NOT_EXISTS:
                    throw new RuntimeException(Translate.id(R.string.product_type_not_exists, type));
                default:
                    throw e;
            }
        }
        
        // Parse result
        Product[] products = result.getArray(ApiConstants.RET_RESULT, Product[].class);
        assertResultNotNull(products, result);
        
        return products;
    }
    
    // Get product details
    public ProductDetails getProductDetails(int productId) {
        JsonResult result;
        try {
            // Call API
            super.needsToken = true;
            result = get(ApiConstants.PRODUCTS_PATH + "/" + productId, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_PRODUCT_NOT_EXISTS:
                    throw new RuntimeException(Translate.id(R.string.product_id_not_found, productId));
                default:
                    throw e;
            }
        }
        
        // Parse result
        ProductDetails details = result.asObject(ProductDetails.class);
        assertResultNotNull(details, result);

        return details;
    }
}
