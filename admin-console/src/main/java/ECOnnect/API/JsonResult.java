package ECOnnect.API;

import com.google.gson.*;

public class JsonResult {
    private static final Gson GSON = new Gson();
    private final JsonObject _jsonObject;
    
    public JsonResult(JsonElement jsonElement) {
        this._jsonObject = jsonElement.getAsJsonObject();
    }
    
    public static JsonResult parse(String jsonString) {
        if (jsonString == null) return null;
        
        JsonResult json = null;
        try {
            json = new JsonResult(JsonParser.parseString(jsonString));
        }
        catch (JsonSyntaxException | IllegalStateException e) {
            throw new RuntimeException("Invalid JSON content:\n" + jsonString);
        }
        return json;
    }
    
    public String getAttribute(String attrName) {
        JsonElement element = _jsonObject.get(attrName);
        if (element == null) return null;
        
        return element.getAsString();
    }
    
    // Get an array of objects
    public <T> T getArray(String attrName, Class<T> arrayClass) {
        if (!arrayClass.isArray()) {
            throw new IllegalArgumentException("classOfT must be an array type");
        }
        
        JsonElement element = _jsonObject.get(attrName);
        if (element == null) return null;
        
        JsonArray array = element.getAsJsonArray();
        if (array == null) return null;
        
        return GSON.fromJson(array, arrayClass);
    }
    
    public <T> T getObject(String attrName, Class<T> objectClass) {
        JsonElement element = _jsonObject.get(attrName);
        if (element == null) return null;
        return GSON.fromJson(element, objectClass);
    }

    public <T> T asObject(Class<T> objectClass) {
        return GSON.fromJson(_jsonObject, objectClass);
    }
    
    
    @Override
    public String toString() {
        return _jsonObject.toString();
    }
    
    // Return the number of attributes in the JsonResult
    public int size() {
        return _jsonObject.size();
    }
}
