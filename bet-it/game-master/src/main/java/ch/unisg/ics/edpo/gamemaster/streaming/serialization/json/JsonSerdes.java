package ch.unisg.ics.edpo.gamemaster.streaming.serialization.json;

import ch.unisg.ics.edpo.gamemaster.streaming.model.joins.GameResult;
import ch.unisg.ics.edpo.gamemaster.streaming.model.joins.UserStats;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotFriendlyFireEvent;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotHitEvent;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotMissEvent;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotSpawnEvent;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.game.DotGame;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.player.Player;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class JsonSerdes {

    public static Serde<DotSpawnEvent> DotSpawnEvent() {
        JsonSerializer<DotSpawnEvent> serializer = new JsonSerializer<>();
        JsonDeserializer<DotSpawnEvent> deserializer = new JsonDeserializer<>(DotSpawnEvent.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
    public static Serde<GameResult> GameResult() {
        JsonSerializer<GameResult> serializer = new JsonSerializer<>();
        JsonDeserializer<GameResult> deserializer = new JsonDeserializer<>(GameResult.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
    public static Serde<DotMissEvent> DotMissEvent() {
        JsonSerializer<DotMissEvent> serializer = new JsonSerializer<>();
        JsonDeserializer<DotMissEvent> deserializer = new JsonDeserializer<>(DotMissEvent.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
    public static Serde<DotFriendlyFireEvent> DotFriendlyFireEvent() {
        JsonSerializer<DotFriendlyFireEvent> serializer = new JsonSerializer<>();
        JsonDeserializer<DotFriendlyFireEvent> deserializer = new JsonDeserializer<>(DotFriendlyFireEvent.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }


    public static Serde<DotHitEvent> DotHitEvent() {
        JsonSerializer<DotHitEvent> serializer = new JsonSerializer<>();
        JsonDeserializer<DotHitEvent> deserializer = new JsonDeserializer<>(DotHitEvent.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
    public static Serde<UserStats> UserStats() {
        JsonSerializer<UserStats> serializer = new JsonSerializer<>();
        JsonDeserializer<UserStats> deserializer = new JsonDeserializer<>(UserStats.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<DotGame> DotGameEnded() {
        JsonSerializer<DotGame> serializer = new JsonSerializer<>();
        JsonDeserializer<DotGame> deserializer = new JsonDeserializer<>(DotGame.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<Player> Player() {
        JsonSerializer<Player> serializer = new JsonSerializer<>();
        JsonDeserializer<Player> deserializer = new JsonDeserializer<>(Player.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

}
