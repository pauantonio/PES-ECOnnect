package com.econnect.client;

import com.econnect.API.Service;
import com.econnect.API.HttpClient.HttpClient;

public class ServiceTestHelper extends Service {

    public static void injectHttpClient(HttpClient client) {
        Service.injectHttpClient(client);
    }
    
    public static void clearToken() {
        try {
            Service.deleteToken();
        }
        catch (IllegalStateException e) {
            // Token was already deleted, ignore
        }
    }
    
    public static void setToken() {
        setToken("dummyToken");
    }
    
    public static void setToken(String token) {
        clearToken();
        Service.setToken(token);
    }
    
}
