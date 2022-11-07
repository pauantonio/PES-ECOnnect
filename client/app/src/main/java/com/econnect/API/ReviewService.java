package com.econnect.API;

import com.econnect.API.Exceptions.ApiException;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

import java.util.TreeMap;

public class ReviewService extends Service{


    ReviewService(){}

    // this works for products and companies
    public void reviewProduct(int productId, int rating) {
        TreeMap<String, String> params = new TreeMap<>();
        // Empty string means all products

        params.put(ApiConstants.REVIEW, String.valueOf(rating));
        super.needsToken = true;

        JsonResult result = null;
        try {
            // Call API
            result = post(ApiConstants.PRODUCTS_PATH + "/" + productId + "/" + ApiConstants.REVIEW, params, null);
        } catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_PRODUCT_NOT_EXISTS:
                    throw new RuntimeException(Translate.id(R.string.product_id_not_found, productId));
                default:
                    throw e;
            }
        }

        // Parse result
        expectOkStatus(result);
    }
}
