package com.vince.retailmanager.repository;

import com.vince.retailmanager.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokensRepository extends JpaRepository<AccessToken, Integer> {
	Optional<AccessToken> findByCompanyId(Integer id);
}
