package ch.unisg.ics.edpo.gamemaster.streaming.serialization.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonSerializer<T> implements Serializer<T> {
    private Gson gson =
            new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

    /** Default constructor needed by Kafka */
    public JsonSerializer() {}

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, T type) {
        System.out.println("serializing: " + type.toString());
        return gson.toJson(type.toString()).getBytes(StandardCharsets.UTF_8);
    }
    @Override
    public void close() {}
}
