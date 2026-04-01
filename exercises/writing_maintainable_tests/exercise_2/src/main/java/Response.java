import java.util.HashMap;
import java.util.Map;

record Response(int statusCode, Object data, Map<String, String> headers) {

    public Response(int statusCode, Object data) {
        this(statusCode, data, new HashMap<>());
        headers.put("Content-Type", "application/json");
    }

    public <T> T getJson(Class<T> clazz) {
        return clazz.cast(data);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
