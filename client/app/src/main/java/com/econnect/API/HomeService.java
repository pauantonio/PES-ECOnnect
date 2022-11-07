package com.econnect.API;

import com.econnect.API.Exceptions.ApiException;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

import java.util.TreeMap;

public class HomeService extends Service{
    HomeService(){}

    public static class Street {
        public final String name;
        public final String[] aliases;

        public Street(String name, String[] aliases) {
            this.name = name;
            this.aliases = aliases;
        }
    }

    public static class Home {
        // Important: The name of these attributes must match the ones in the returned JSON
        public final String numero;
        public final String escala;
        public final String pis;
        public final String porta;
        public final String num_cas;
        public final String carrer;

        public Home(String numero, String escala, String pis, String porta, String num_cas, String carrer) {
            this.numero = numero;
            this.escala = escala;
            this.pis = pis;
            this.porta = porta;
            this.num_cas = num_cas;
            this.carrer = carrer;
        }
    }


    public Home[] getHomesBuilding(String zipcode, Street street, String num) {
        StringBuilder street_aliases = new StringBuilder();
        boolean first = true;
        for (String alias : street.aliases) {
            if (first) first = false;
            else street_aliases.append("#");
            street_aliases.append(alias);
        }

        JsonResult result;
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.ZIPCODE, zipcode);
        params.put(ApiConstants.STREET_NAME, street_aliases.toString());
        params.put(ApiConstants.STREET_NUM, num);
        // Call API
        try{
            super.needsToken = true;
            result = get(ApiConstants.BUILDING_PATH, params);
        } catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_BUILDING_NOT_EXISTS:
                    throw new RuntimeException(Translate.id(R.string.building_not_exists));
                default:
                    throw e;
            }
        }

        // Parse result
        Home[] homes = result.getArray(ApiConstants.RET_RESULT, Home[].class);
        assertResultNotNull(homes, result);
        return homes;
    }

    public Street[] getCity(String zipcode) {
        JsonResult result;
        // Call API
        try{
            super.needsToken = true;
            result = get(ApiConstants.CITY_PATH + "/" + zipcode, null);
        } catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_CITY_NOT_EXISTS:
                    throw new RuntimeException(Translate.id(R.string.city_not_exists, zipcode));
                default:
                    throw e;
            }
        }

        // Parse result
        Street[] s = result.getArray(ApiConstants.RET_RESULT, Street[].class);
        assertResultNotNull(s, result);
        return s;
    }

    public void setHome(String zipcode, Home home) {
        JsonResult result;
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.ZIPCODE, zipcode);
        params.put(ApiConstants.STREET_NAME, home.carrer);
        params.put(ApiConstants.STREET_NUM, home.numero);
        if (home.escala != null) params.put(ApiConstants.ESCALA, home.escala);
        if (home.pis != null) params.put(ApiConstants.FLOOR, home.pis);
        if (home.porta != null) params.put(ApiConstants.DOOR, home.porta);
        // Call API
        try{
            super.needsToken = true;
            result = put(ApiConstants.HOME_PATH, params,null);
        } catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_BUILDING_NOT_EXISTS:
                    throw new RuntimeException(Translate.id(R.string.building_not_exists));
                default:
                    throw e;
            }
        }
        // Parse result
        expectOkStatus(result);
    }
}
