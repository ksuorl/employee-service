package com.example.employeeservice.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
//@ConfigurationProperties("ems.kafka")
public class KafkaProducerConfig {
//    @NotNull
//    private String bootstrapServers;
//    @NotNull
//    private String groupId;
//    @NotNull
//    private String topic;
//
//    public String getBootstrapServers() {
//        return bootstrapServers;
//    }
//
//    public String getGroupId() {
//        return groupId;
//    }
//
//    public String getTopic() {
//        return topic;
//    }

    //    @NotNull
//    private String keyDeserializerClass;
//    @NotNull
//    private String valueDeserializerClass;



    @Bean
    public ProducerFactory<String, Object> producerFactory() {
//FIXME
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
