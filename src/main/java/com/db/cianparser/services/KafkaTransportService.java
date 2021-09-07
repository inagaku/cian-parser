package com.db.cianparser.services;

import com.db.cianparser.config.KafkaConfiguration;
import com.db.cianparser.model.Advertisement;
import com.db.cianparser.services.connectors.CianConnectionService;
import com.db.cianparser.services.parsers.CianExtractorService;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaTransportService {

    @Autowired
    CianExtractorService extractorService;

    @Autowired
    KafkaTemplate<String, Advertisement> kafkaTemplate;

    @SneakyThrows
    @Scheduled(cron = "*/30 * * * * *")
    public void TransferJsonArrayToKafka() {
        List<Advertisement> advertisements = extractorService.extract();
        System.out.println("Iteration");
        if (advertisements != null) {
            advertisements.forEach(ad -> kafkaTemplate.send(KafkaConfiguration.TOPIC, ad));
        }
    }
}
