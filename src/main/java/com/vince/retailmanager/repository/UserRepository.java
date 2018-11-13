package com.vince.retailmanager.repository;

import com.vince.retailmanager.entity.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Profile("spring-data-jpa")
public interface UserRepository extends JpaRepository<User, Integer> {

	//    void save(User user) throws DataAccessException;
	Optional<User> findByUsername(String username);
}
