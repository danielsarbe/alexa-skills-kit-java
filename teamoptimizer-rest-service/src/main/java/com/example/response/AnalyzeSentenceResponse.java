package com.example.response;

/**
 * Created by opetridean on 5/21/16.
 */
public class AnalyzeSentenceResponse {

    private String sentence;

    private int score;

    private String sentiment;

    public AnalyzeSentenceResponse() {
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}
