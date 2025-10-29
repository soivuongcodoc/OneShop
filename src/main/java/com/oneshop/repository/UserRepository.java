package com.oneshop.repository;

import com.oneshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> admin
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
<<<<<<< HEAD
=======
  List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);
>>>>>>> admin
}
