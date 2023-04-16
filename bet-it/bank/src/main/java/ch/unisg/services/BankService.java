package ch.unisg.services;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Properties;

@Service
public class BankService {

    private final String STRIPE = "stripe";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final Serde<String> STRING_SERDE = Serdes.String();


    public boolean testService(String from, String to, int amount){
        //if from is stripe, we can consider it as a credit card payment, which always works
        if(from.equals(STRIPE)){
            //check only to
            log.info("checking to");
            String toLower = to.toLowerCase();
            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-kafka-streams-app");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");

            StreamsBuilder streamsBuilder = new StreamsBuilder();
            KStream<String, String> messageStream = streamsBuilder
                    .stream("bet.added-new-customer", Consumed.with(STRING_SERDE, STRING_SERDE));


            KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);
            streams.start();
            var kTable = messageStream
//                    .filter((key, value) -> key.equals("name") && value.toLowerCase().equals(toLower))
                    .toTable();

            kTable.toStream().foreach((k,v) -> log.info("key: {}, value: {}", k,v));
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
            log.info("check over");
        }else{
            // check from and to
        }
        // test whether user from and to exists

        // test whether from has this amount of money

        return true;
    }
}
