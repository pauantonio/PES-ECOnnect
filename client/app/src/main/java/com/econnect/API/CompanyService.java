package com.econnect.API;

import android.graphics.Bitmap;

import com.econnect.API.Exceptions.ApiException;
import com.econnect.Utilities.BitmapLoader;
import com.econnect.API.ProductService.ProductDetails.Question;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

import java.util.Locale;

public class CompanyService extends Service {
    
    // Only allow instantiating from ServiceFactory
    CompanyService() {}
    
    public static class Company implements IAbstractProduct {
        // Important: The name of these attributes must match the ones in the returned JSON
        // Gson will initialize these fields to the received values
        public final int id;
        public final String name;
        public final float avgrating;
        public final String imageurl;
        public final double lat;
        public final double lon;
        private Bitmap imageBitmap = null;

        public Company(int id, String name, float avgRating, String imageURL, double lat, double lon) {
            this.id = id;
            this.name = name;
            this.avgrating = avgRating;
            this.imageurl = imageURL;
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        public String getName() {
            return name;
        }
        @Override
        public String getSecondaryText() {
            // Display coordinates in format: "12.3456N 34.5678W"
            String latStr, lonStr;
            if (lat >= 0) latStr = String.format(Locale.getDefault(), "%.04fN", lat);
            else latStr = String.format(Locale.getDefault(), "%.04fS", -lat);
            if (lon >= 0) lonStr = String.format(Locale.getDefault(), "%.04fE", lon);
            else lonStr = String.format(Locale.getDefault(), "%.04fW", -lon);

            return latStr + ", " + lonStr;
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
        public boolean hasImage() {
            return imageBitmap != null;
        }
    }

    public static class CompanyDetails {
        // Important: The name of these attributes must match the ones in the returned JSON
        // Gson will initialize these fields to the received values
        public final String imageURL;
        public final double latitude;
        public final double longitude;
        public final String name;
        public final Question[] questions;
        public final int[] ratings;
        public int userRate;
        public CompanyDetails(String imageURL, double latitude, double longitude, String name, Question[] questions, int[] ratings, int userRate) {
            this.imageURL = imageURL;
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
            this.questions = questions;
            this.ratings = ratings;
            this.userRate = userRate;
        }

        public Question getQuestion(int id) {
            for (Question q : questions) {
                if (q.questionid == id)
                    return q;
            }
            return null;
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
    public String[] getQuestions() {
        
        // Call API
        super.needsToken = true;
        JsonResult result = get(ApiConstants.COMPANY_QUESTIONS_PATH, null);
        
        // Parse result
        String[] questions = result.getArray(ApiConstants.RET_RESULT, String[].class);
        assertResultNotNull(questions, result);
        
        return questions;
    }

    // Get company details
    public CompanyDetails getCompanyDetails(int companyId) {
        JsonResult result;
        try {
            // Call API
            super.needsToken = true;
            result = get(ApiConstants.COMPANIES_PATH + "/" + companyId, null);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_COMPANY_NOT_EXISTS:
                    throw new RuntimeException(Translate.id(R.string.company_does_not_exist, companyId));
                default:
                    throw e;
            }
        }

        // Parse result
        CompanyDetails details = result.asObject(CompanyDetails.class);
        assertResultNotNull(details, result);

        return details;
    }
}
