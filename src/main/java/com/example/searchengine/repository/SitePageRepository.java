package com.example.searchengine.repository;

import com.example.searchengine.entity.SitePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SitePageRepository extends JpaRepository<SitePage, Long> {

    @Query("SELECT sp FROM SitePage sp WHERE LOWER(sp.title) LIKE %:searchString% OR LOWER(sp.keywords) LIKE %:searchString% OR LOWER(sp.description) LIKE %:searchString%")
    List<SitePage> findByTitleOrKeywordOrDescriptionContaining(String searchString);
    Optional<SitePage> findByUrl(String url);
}
