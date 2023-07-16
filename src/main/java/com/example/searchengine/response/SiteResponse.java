package com.example.searchengine.response;

import com.example.searchengine.entity.Site;
import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SiteResponse extends Response {
    private Site site;

    public SiteResponse(ResponseStatus status, String message, Site site){
        super(status,message);
        this.site = site;
    }
}
