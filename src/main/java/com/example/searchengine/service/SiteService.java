package com.example.searchengine.service;

import com.example.searchengine.entity.Site;
import com.example.searchengine.entity.User;
import com.example.searchengine.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SiteService {

    @Autowired
    private SiteRepository siteRepository;

    public Site save(Site site, User user) {
        site.setUser(user);
        return siteRepository.save(site);
    }
}
