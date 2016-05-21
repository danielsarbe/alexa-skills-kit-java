package com.example.controller;

import com.example.service.SentimentAnalysisService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class AnalyzeController {
    public static final String JSON_STATUS_SUCCESS = "0";
    public static final String JSON_STATUS_FAIL = "-1";

    @Autowired
    SentimentAnalysisService sentimentAnalysisService;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "John") String name, Model model) {
        model.addAttribute("name", name);

        AnalyzeResponse analyzeResponse = new AnalyzeResponse();
        analyzeResponse.setText(name + "-" + System.currentTimeMillis());
        analyzeResponse.setSentences(new String[]{"s1", "s2", System.nanoTime() + "b"});

        model.addAttribute("analyzeResponse", analyzeResponse);

        return "greeting";
    }

    @RequestMapping(value = "/analyze", method = RequestMethod.GET, produces = "application/json")
    public AnalyzeResponse analyzeText(@RequestParam(value = "text", required = false, defaultValue = "Hello World") String text, Model model) {
        if (text == null || text.equals(""))
            return null;

        List<String> list = new LinkedList();


        try {
            BufferedReader reader = new BufferedReader(new StringReader(text));
            String line = null;
            while ((line = reader.readLine()) != null) {
                list.add(sentimentAnalysisService.analyzeSentence(line).toString());
            }

            Map object = new HashMap();
            object.put("list", list);

            model.addAttribute("name", String.join(", ", list));
            AnalyzeResponse analyzeResponse = new AnalyzeResponse();
            analyzeResponse.setText(text);
            analyzeResponse.setSentences(new String[]{"s1", "s1", System.nanoTime() + "b"});

            return analyzeResponse;

        } catch (IOException ex) {
            Logger.getLogger(AnalyzeController.class.getName()).log(Level.SEVERE, "AdminController: could not get uploaded file bytes", ex);
            return null;
        }
    }


    public static String toStatusJson(String status, String message, Object object) {
        Map map = new HashMap();
        map.put("status", status);
        map.put("message", message);
        map.put("object", object);

        Gson gson = getGson();
        String json = gson.toJson(map);
        return json;
    }

    //create gson with custom serializers
    private static Gson getGson() {
        return new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm").create();
    }


}
