package com.example.searchengine.helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class HtmlParser {

    private Document document;

    public void loadDocument(String html){
        document = Jsoup.parse(html);
    }

    public String getTitle(){
        return document.title();
    }

    public String getDescription(){
        Element descriptionElement = document.selectFirst("meta[name=description]");
        if(descriptionElement != null){
            return descriptionElement.attr("content");
        }
        return "";
    }

    public String getKeywords(){
        Element keywordsElement = document.selectFirst("meta[name=keywords]");
        if(keywordsElement != null){
            return keywordsElement.attr("content");
        }
        return "";
    }

}