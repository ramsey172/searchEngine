package com.example.searchengine.response;

import com.example.searchengine.DTO.SitePageDTO;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SearchResponse extends Response {
    private List<SitePageDTO> sitePageDTOs;

    public SearchResponse(ResponseStatus status, String message, List<SitePageDTO> sitePageDTOs){
        super(status,message);
        this.sitePageDTOs = sitePageDTOs;
    }
}
