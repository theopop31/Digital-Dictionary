package com.company;

import java.util.ArrayList;

public class Definition implements Comparable<Definition>{
    private String dict;
    private String dictType;
    private int year;
    private ArrayList<String> text;

    public Definition(String dict, String dictType, int year, ArrayList<String> text) {
        this.dict = dict;
        this.dictType = dictType;
        this.year = year;
        this.text = text;
    }

    public String getDict() {
        return dict;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public String getDictType() {
        return dictType;
    }

    public int getYear() {
        return year;
    }

    @Override
    public int compareTo(Definition o) {
        int compareYear = o.getYear();
        return this.year - compareYear;
    }

    public void printDefinition() {
        System.out.println(this.getDict() + " - " +
                            this.getDictType() + " - " +
                            this.getYear()
                            );
    }
}
