package com.example.searchengine.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class SitePageDTO {
    private String url;
    public SitePageDTO(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
}
