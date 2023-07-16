package com.example.searchengine.service;

import com.example.searchengine.entity.SitePage;
import com.example.searchengine.repository.SitePageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SitePageService {

    @Autowired
    private SitePageRepository sitePageRepository;

    public void save(SitePage sitePage){
        if(!sitePage.getTitle().isBlank()){
            sitePageRepository.save(sitePage);
        }
    }

    public Optional<SitePage> findByUrl(String url){
        return sitePageRepository.findByUrl(url);
    }

}
