package com.example.searchengine.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "site_page")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SitePage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "site_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Site site;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private String title;

    private String description;

    private String keywords;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public SitePage(String url, Site site, String title, String description, String keywords){
        this.url = url;
        this.site = site;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}