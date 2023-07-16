package com.example.searchengine.repository;

import com.example.searchengine.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    boolean existsByUrl(String url);
    Set<Site> findByIsScannedFalse();
}
