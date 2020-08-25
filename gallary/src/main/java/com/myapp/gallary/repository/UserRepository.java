package com.myapp.gallary.repository;

import com.myapp.gallary.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    //Optional<User> findOneByEmail(String email);
}
