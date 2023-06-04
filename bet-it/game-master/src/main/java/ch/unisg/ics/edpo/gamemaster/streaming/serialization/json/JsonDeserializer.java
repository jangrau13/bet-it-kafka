package ch.unisg.ics.edpo.gamemaster.streaming.serialization.json;


import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;


import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
public class JsonDeserializer<T> implements Deserializer<T> {
    private Gson gson =
            new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .serializeSpecialFloatingPointValues()
                    .create();

    private Class<T> destinationClass;
    private Type reflectionTypeToken;

    /** Default constructor needed by Kafka */
    public JsonDeserializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    public JsonDeserializer(Type reflectionTypeToken) {
        this.reflectionTypeToken = reflectionTypeToken;
    }

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {}

    @Override
    public T deserialize(String topic, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Type type = destinationClass != null ? destinationClass : reflectionTypeToken;
        try {
            return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), type);
        } catch (JsonSyntaxException e) {
            throw new CustomDeserilizationError("Failed to deserialize data for topic: " + topic + " and for value " + new String(bytes) + " and type: " + type, e);
        }
    }


    @Override
    public void close() {}
}
