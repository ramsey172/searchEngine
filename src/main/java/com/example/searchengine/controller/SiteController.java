package com.example.searchengine.controller;

import com.example.searchengine.entity.Site;
import com.example.searchengine.entity.User;
import com.example.searchengine.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/site")
public class SiteController {
    @Autowired
    private SiteService siteService;

    @PostMapping
    public ResponseEntity<Site> add(Authentication authentication, @RequestBody Site site) {
        User user = (User) authentication.getPrincipal();
        Site returnSite = siteService.save(site,user);
        return ResponseEntity.ok(returnSite);
    }
}

