package com.listin.ListIn.repository;

import com.listin.ListIn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByApiKey(String apiKey);

    public Optional<User> findByUsername(String username);
}
