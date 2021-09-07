package com.db.cianparser.services.parsers;

import com.db.cianparser.model.Advertisement;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ExtractorService {
    List<Advertisement> extract();
}
