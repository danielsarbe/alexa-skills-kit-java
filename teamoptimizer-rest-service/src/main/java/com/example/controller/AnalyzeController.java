package com.example.controller;

import com.basistech.rosette.api.RosetteAPI;
import com.basistech.rosette.api.RosetteAPIException;
import com.basistech.rosette.apimodel.SentimentResponse;
import com.example.response.AnalyzeResponse;
import com.example.response.AnalyzeSentenceResponse;
import com.example.service.SentimentAnalysisService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class AnalyzeController {

    @Autowired
    SentimentAnalysisService sentimentAnalysisService;

//    @RequestMapping("/greeting")
//    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "John") String name, Model model) {
//        model.addAttribute("name", name);
//
//        AnalyzeResponse analyzeResponse = new AnalyzeResponse();
////        analyzeResponse.setText(name + "-" + System.currentTimeMillis());
////        analyzeResponse.setSentences(new String[]{"s1", "s2", System.nanoTime() + "b"});
//
//        model.addAttribute("analyzeResponse", analyzeResponse);
//
//        return "greeting";
//    }


    @RequestMapping(value = "/analyze2", method = RequestMethod.POST, produces = "application/json")
    public AnalyzeResponse analizeText(@RequestBody @NotEmpty String text) {
        AnalyzeResponse analyzeResponse = new AnalyzeResponse();
        List<AnalyzeSentenceResponse> analyzeSentenceResponses = new ArrayList<>();


        Annotation annotation = sentimentAnalysisService.getPipeline().process(text);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            AnalyzeSentenceResponse analyzeSentenceResponse = new AnalyzeSentenceResponse();
            analyzeSentenceResponse.setSentence(sentence.toString());
            analyzeSentenceResponse.setSentiment(sentiment);
            analyzeSentenceResponse.setScore(sentimentAnalysisService.analyzeSentence(sentence.toString()));
            analyzeSentenceResponses.add(analyzeSentenceResponse);
        }

        analyzeResponse.setAnalyzeSentenceResponses(analyzeSentenceResponses);
        return analyzeResponse;
    }

    @RequestMapping(value = "/analyzeR", method = RequestMethod.POST, produces = "application/json")
    public AnalyzeResponse analizeTextRosette(@RequestBody @NotEmpty String text) throws IOException, RosetteAPIException {
        AnalyzeResponse analyzeResponse = new AnalyzeResponse();
        List<AnalyzeSentenceResponse> analyzeSentenceResponses = new ArrayList<>();


        RosetteAPI rosetteApi = new RosetteAPI("35056fa31ad5ecfb39e419b8eb10c20e",  "https://api.rosette.com/rest/v1");
        SentimentResponse response = rosetteApi.getSentiment(IOUtils.toInputStream(text, "UTF-8"), "text/html", null, null);




        return analyzeResponse;
    }



}
