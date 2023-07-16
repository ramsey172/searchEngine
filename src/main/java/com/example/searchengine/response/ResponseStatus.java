package com.example.searchengine.response;

public enum ResponseStatus {
    ERROR("error"),
    SUCCESS("success");

    private final String value;
    ResponseStatus(String value){
        this.value = value;
    }
    public String getValue(){ return value;}
}
