package com.example.searchengine.task;

import com.example.searchengine.entity.Site;
import com.example.searchengine.entity.SitePage;
import com.example.searchengine.helper.HtmlParser;
import com.example.searchengine.helper.SitemapParser;
import com.example.searchengine.service.SitePageService;
import com.example.searchengine.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Component
public class SiteCrawlerTask implements Task {
    @Value("${sitecrawler.cron.expression}")
    private String cronExpression;
    @Autowired
    SiteService siteService;
    @Autowired
    SitePageService sitePageService;
    @Autowired
    SitemapParser sitemapParser;
    @Autowired
    HtmlParser htmlParser;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private static final String SITEMAP_PATH = "/sitemap.xml";

    @Scheduled(cron = "${sitecrawler.cron.expression}")
    public void execute() {
        Set<Site> sites = siteService.findByIsScannedFalse();
        if (!sites.isEmpty()) {
            for (Site site : sites) {
                try {
                    ResponseEntity<String> sitemapResponse = restTemplateBuilder.build().exchange(site.getUrl() + SITEMAP_PATH, HttpMethod.GET, null, String.class);
                    if (sitemapResponse.getStatusCode().is2xxSuccessful()) {
                        List<String> urls = sitemapParser.parseUrls(sitemapResponse.getBody());
                        if (!urls.isEmpty()) {
                            for (String url : urls) {
                                try {
                                    ResponseEntity<String> pageResponse = restTemplateBuilder.build().getForEntity(url, String.class);
                                    if (pageResponse.getStatusCode().is2xxSuccessful()) {
                                        htmlParser.loadDocument(pageResponse.getBody());
                                        Optional<SitePage> oSitePage = sitePageService.findByUrl(url);
                                        SitePage sitePage;
                                        if(oSitePage.isPresent()){
                                            sitePage = oSitePage.get();
                                            sitePage.setTitle(htmlParser.getTitle());
                                            sitePage.setDescription(htmlParser.getDescription());
                                            sitePage.setKeywords(htmlParser.getKeywords());
                                        }else{
                                            sitePage = new SitePage(url, site, htmlParser.getTitle(), htmlParser.getDescription(), htmlParser.getKeywords());
                                        }
                                        sitePageService.save(sitePage);
                                    }
                                } catch (HttpClientErrorException.NotFound e) {

                                }
                            }
                        }
                    }
                } catch (HttpClientErrorException.NotFound e) {

                }
                site.setScanned(true);
                siteService.save(site);
            }
        }
    }

}
