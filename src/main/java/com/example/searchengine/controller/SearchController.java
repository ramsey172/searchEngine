package com.example.searchengine.controller;

import com.example.searchengine.DTO.SitePageDTO;
import com.example.searchengine.response.Response;
import com.example.searchengine.response.ResponseStatus;
import com.example.searchengine.response.SearchResponse;
import com.example.searchengine.service.SearchService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    private static final String NOTHING_FOUND = "Not found";

    @GetMapping
    public ResponseEntity<Response> search(@RequestParam @NotBlank String searchString) {
        List<SitePageDTO> searchResults = searchService.getResults(searchString);
        if(searchResults.isEmpty()){
            return ResponseEntity.ok().body(new Response(ResponseStatus.SUCCESS,NOTHING_FOUND));
        }
        return ResponseEntity.ok().body(new SearchResponse(ResponseStatus.SUCCESS,"Results for query - "+searchString, searchResults));
    }
}

