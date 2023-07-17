package com.example.searchengine.controller;

import com.example.searchengine.entity.Site;
import com.example.searchengine.entity.User;
import com.example.searchengine.response.Response;
import com.example.searchengine.response.ResponseStatus;
import com.example.searchengine.response.SiteResponse;
import com.example.searchengine.service.SiteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/site")
public class SiteController {
    @Autowired
    private SiteService siteService;
    private static final String SITE_CREATED_SUCCESS_MESSAGE = "Site created";
    private static final String SITE_NOT_FOUND = "Site is not found";
    private static final String SAME_URL_ERROR_MESSAGE = "A site with the same name already exists";
    private static final String UNEXPECTED_SITE_ADD_ERROR_MESSAGE = "An error occurred while adding site";

    @PostMapping
    public ResponseEntity<Response> add(Authentication authentication, @Valid @RequestBody Site site, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            String errorMessage = errors.stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new Response(ResponseStatus.ERROR, errorMessage));
        }
        try {
            if (siteService.existsByUrl(site.getUrl())) {
                return ResponseEntity.badRequest().body(new Response(ResponseStatus.ERROR,SAME_URL_ERROR_MESSAGE));
            }
            User user = (User) authentication.getPrincipal();
            site.setUser(user);
            return ResponseEntity.ok(new SiteResponse(ResponseStatus.SUCCESS, SITE_CREATED_SUCCESS_MESSAGE, siteService.saveAndFlush(site)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(ResponseStatus.ERROR, UNEXPECTED_SITE_ADD_ERROR_MESSAGE));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(Authentication authentication, @PathVariable long id) {
        User user = (User) authentication.getPrincipal();
        Optional<Site> oSite = siteService.findById(id);
        if(oSite.isPresent()){
            Site site = oSite.get();
            if(site.getUser().getId() == user.getId()){
                return ResponseEntity.ok().body(new SiteResponse(ResponseStatus.SUCCESS,"",site));
            }
        }
        return ResponseEntity.badRequest().body(new Response(ResponseStatus.ERROR,SITE_NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteById(Authentication authentication, @PathVariable long id) {
        User user = (User) authentication.getPrincipal();
        Optional<Site> oSite = siteService.findById(id);
        if(oSite.isPresent()){
            Site site = oSite.get();
            if(site.getUser().getId() == user.getId()){
                siteService.deleteById(site.getId());
                return ResponseEntity.ok().body(new SiteResponse(ResponseStatus.SUCCESS,"deleted",site));
            }
        }
        return ResponseEntity.badRequest().body(new Response(ResponseStatus.ERROR,SITE_NOT_FOUND));
    }

    @PostMapping("/{id}/reindex")
    public ResponseEntity<Response> reindexById(Authentication authentication, @PathVariable long id){
        User user = (User) authentication.getPrincipal();
        Optional<Site> oSite = siteService.findById(id);
        if(oSite.isPresent()){
            Site site = oSite.get();
            if(site.getUser().getId() == user.getId()){
                site.setScanned(false);
                siteService.save(site);
                return ResponseEntity.ok().body(new SiteResponse(ResponseStatus.SUCCESS,"added to reindex queue",site));
            }
        }
        return ResponseEntity.badRequest().body(new Response(ResponseStatus.ERROR,SITE_NOT_FOUND));
    }
}

