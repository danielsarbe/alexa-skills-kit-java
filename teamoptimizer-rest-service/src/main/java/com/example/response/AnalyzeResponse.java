package com.example.response;

import java.io.Serializable;
import java.util.List;

public class AnalyzeResponse implements Serializable {

    private List<AnalyzeSentenceResponse> analyzeSentenceResponses;

    public List<AnalyzeSentenceResponse> getAnalyzeSentenceResponses() {
        return analyzeSentenceResponses;
    }

    public void setAnalyzeSentenceResponses(List<AnalyzeSentenceResponse> analyzeSentenceResponses) {
        this.analyzeSentenceResponses = analyzeSentenceResponses;
    }
}
