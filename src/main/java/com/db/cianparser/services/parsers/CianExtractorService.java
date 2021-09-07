package com.db.cianparser.services.parsers;

import com.db.cianparser.config.CianConnectionOptions;
import com.db.cianparser.model.Advertisement;
import com.db.cianparser.services.connectors.CianConnectionService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;


@Service
public class CianExtractorService implements ExtractorService {

    private final int CIAN_RETRY = 3;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CianConnectionService connectService;

    @SneakyThrows
    public List<Advertisement> extract() {

        HttpURLConnection connection = connectService.connect();

        connection.setRequestMethod("GET");

        InputStream response = connection.getInputStream();

        JsonNode dataNode = objectMapper.readTree(response).get("data");

        if (dataNode == null) {
            return null;
        }

        List<Advertisement> advertisements = objectMapper.readValue(dataNode.toString(), new TypeReference<List<Advertisement>>(){});

        advertisements.stream()
                .forEach(ad -> ad.setUrl(CianConnectionOptions.FLAT_URL_PREFIX + Long.toString((ad.getId()))));

        return advertisements;
    }
}

