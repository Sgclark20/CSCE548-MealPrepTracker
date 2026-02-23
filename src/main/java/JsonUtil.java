import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDate;
import java.time.LocalTime;

public class JsonUtil {

    private JsonUtil() {}

    public static Gson gson() {
        JsonSerializer<LocalDate> localDateSer =
                (src, typeOfSrc, context) -> new JsonPrimitive(src.toString());
        JsonDeserializer<LocalDate> localDateDes =
                (json, typeOfT, context) -> LocalDate.parse(json.getAsString());

        JsonSerializer<LocalTime> localTimeSer =
                (src, typeOfSrc, context) -> new JsonPrimitive(src.toString());
        JsonDeserializer<LocalTime> localTimeDes =
                (json, typeOfT, context) -> LocalTime.parse(json.getAsString());

        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateSer)
                .registerTypeAdapter(LocalDate.class, localDateDes)
                .registerTypeAdapter(LocalTime.class, localTimeSer)
                .registerTypeAdapter(LocalTime.class, localTimeDes)
                .create();
    }
}