package com.example.controller;

import java.io.Serializable;

public class AnalyzeResponse implements Serializable {
    private String text;
    private String[] sentences;
    private Integer[] score;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getSentences() {
        return sentences;
    }

    public void setSentences(String[] sentences) {
        this.sentences = sentences;
    }

    public Integer[] getScore() {
        return score;
    }

    public void setScore(Integer[] score) {
        this.score = score;
    }
}
