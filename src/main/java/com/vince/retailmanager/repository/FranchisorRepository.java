package com.vince.retailmanager.repository;

import com.vince.retailmanager.entity.Franchisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchisorRepository extends JpaRepository<Franchisor, Integer> {
//	//find franchisor by Username
//	@Query("select f from Franchisor where f.id   ")
//	Optional<Franchisor> findFranchisorByUsername();
}
