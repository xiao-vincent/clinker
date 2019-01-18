package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.authorization.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);

  boolean existsByUsernameIgnoreCase(String username);
}
