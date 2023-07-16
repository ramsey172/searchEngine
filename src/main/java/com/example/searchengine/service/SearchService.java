package com.example.searchengine.service;

import com.example.searchengine.DTO.SitePageDTO;
import com.example.searchengine.entity.SitePage;
import com.example.searchengine.repository.SitePageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SearchService {

    @Autowired
    private SitePageRepository sitePageRepository;

    public List<SitePageDTO> getResults(String searchString){
        List<SitePage> searchResults = sitePageRepository.findByTitleOrKeywordOrDescriptionContaining(searchString);
        List<SitePageDTO> dtoList = new ArrayList<>();

        for (SitePage sitePage : searchResults) {
            dtoList.add(new SitePageDTO(sitePage.getUrl()));
        }
        return dtoList;
    }

}
