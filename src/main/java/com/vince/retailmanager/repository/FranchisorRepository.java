package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.Franchisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchisorRepository extends JpaRepository<Franchisor, Integer> {

  boolean existsByNameIgnoreCase(String name);

  boolean existsByWebsiteIgnoreCase(String website);
}
