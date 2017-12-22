package com.codecool.enterprise.ai.controller;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
public class AIController {

    @GetMapping("/ai")
    public Integer aiService(@ModelAttribute("table") String table) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response =
                    restTemplate.getForEntity("http://tttapi.herokuapp.com/api/v1/" + table + "/X", String.class);
            JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
            return (Integer) jacksonJsonParser.parseMap(response.getBody()).get("recommendation");
        } catch (ResourceAccessException e) {
            System.out.println("AI Service is unavailable: " + e);
            return null;
        }
    }
}
