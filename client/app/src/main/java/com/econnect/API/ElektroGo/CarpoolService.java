package com.econnect.API.ElektroGo;

import com.econnect.API.ApiConstants;
import com.econnect.API.Exceptions.ApiException;
import com.econnect.API.Exceptions.InvalidTokenApiException;
import com.econnect.API.ImageUpload.ApacheUploadClient;
import com.econnect.API.ImageUpload.IUploadClient;
import com.econnect.API.JsonResult;
import com.econnect.API.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.TreeMap;

public class CarpoolService extends Service {
    
    public static class CarpoolPoint {
        public int id;
        public String startDate;
        public String startTime;
        public int offeredSeats;
        public int occupiedSeats;
        public String restrictions;
        public String details;
        public String vehicleNumberPlate;
        public String origin;
        public String destination;
        public String username;
        public String cancelDate;
        public double latitudeOrigin;
        public double longitudeOrigin;
        public double latitudeDestination;
        public double longitudeDestination;
    }

    public boolean pingServer() {
        try {
            getRaw(ElektroGoAPIConstants.ELEKTROGO_BASE_URL, null);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    // Get points from ElekroGo server
    public CarpoolPoint[] getPoints(double lat, double lon, float radiusKm) throws ApiException {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ElektroGoAPIConstants.CARPOOL_PARAM_TOKEN, ApiKey.ELEKTROGO_TOKEN);
        params.put(ElektroGoAPIConstants.CARPOOL_PARAM_LAT, Double.toString(lat));
        params.put(ElektroGoAPIConstants.CARPOOL_PARAM_LON, Double.toString(lon));
        params.put(ElektroGoAPIConstants.CARPOOL_PARAM_RADIUS, Float.toString(radiusKm));
        
        // Call API
        final String URL = ElektroGoAPIConstants.ELEKTROGO_BASE_URL + ElektroGoAPIConstants.CARPOOL_PATH;
        String result = getRaw(URL, params);

        JsonArray arr = JsonParser.parseString(result).getAsJsonArray();

        return new Gson().fromJson(arr, CarpoolPoint[].class);
    }

}
