package com.example.searchengine.aspect;

import com.example.searchengine.service.SiteService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SiteCrawlerLoggingAspect {

    @Autowired
    SiteService siteService;

    private final Logger logger = LoggerFactory.getLogger(SiteCrawlerLoggingAspect.class);

    private int indexedSitesCountBefore = 0;

    @Pointcut("execution(* com.example.searchengine.task.SiteCrawlerTask.execute(..))")
    public void siteCrawlerTask() {
    }

    @Before("siteCrawlerTask()")
    public void logBefore() {
        this.indexedSitesCountBefore = siteService.findByIsScannedTrue().size();
        logger.info("Start the execution of siteCrawler");
    }

    @AfterReturning("execution(* com.example.searchengine.task.SiteCrawlerTask.execute(..))")
    public void afterExecute() {
        logger.info("End of execution of siteCrawler, count: {}", siteService.findByIsScannedTrue().size() - indexedSitesCountBefore);
    }
}