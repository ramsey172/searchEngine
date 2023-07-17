package com.example.searchengine.service;

import com.example.searchengine.entity.Site;
import com.example.searchengine.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
public class SiteService {

    @Autowired
    private SiteRepository siteRepository;

    public Site saveAndFlush(Site site){
        return siteRepository.saveAndFlush(site);
    }

    public boolean existsByUrl(String url){
        return siteRepository.existsByUrl(url);
    }

    public void deleteById(long id){
        siteRepository.deleteById(id);
    }

    public void save(Site site){
        siteRepository.save(site);
    }

    public Set<Site> findByIsScannedFalse(){
        return siteRepository.findByIsScannedFalse();
    }
    public Set<Site> findByIsScannedTrue(){
        return siteRepository.findByIsScannedTrue();
    }

    public Optional<Site> findById(long id){
        return siteRepository.findById(id);
    }
}
