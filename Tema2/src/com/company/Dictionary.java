package com.company;

import java.util.ArrayList;

public class Dictionary {
    private String language;
    private ArrayList<Word> words;

    public Dictionary(String language, ArrayList<Word> words) {
        this.language = language;
        this.words = words;
    }

    public String getLanguage() {
        return language;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    Word searchWord(String word) {
        for (Word w : this.getWords()) {
            if (word.equals(w.getWord())) {
                return w;
            }
        }
        System.out.println("Cuvantul nu exista in dictionar");
        return null;
    }
}
