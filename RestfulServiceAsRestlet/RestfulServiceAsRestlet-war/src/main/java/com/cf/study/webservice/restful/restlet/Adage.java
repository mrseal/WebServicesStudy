package com.cf.study.webservice.restful.restlet;

public class Adage {

    private String words;
    private int wordCount;
    private int id;

    @Override
    public String toString() {
        return String.format("%2d: ", id) + words + " -- [" + wordCount + " words]";
    }

    public void setWords(final String words) {
        this.words = words;
        wordCount = words.trim().split("\\s+").length;
    }

    public String getWords() {
        return words;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(final int wordCount) {
        this.wordCount = wordCount;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

}
