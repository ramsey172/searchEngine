package com.example.searchengine.helper;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class SitemapParser {
    public List<String> parseUrls(String xmlString) {
        List<String> urls = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));

            Element rootElement = document.getDocumentElement();
            NodeList urlNodes = rootElement.getElementsByTagName("url");

            for (int i = 0; i < urlNodes.getLength(); i++) {
                Element urlElement = (Element) urlNodes.item(i);
                String url = urlElement.getElementsByTagName("loc").item(0).getTextContent();
                urls.add(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return urls;
    }
}
