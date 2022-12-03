package com.example.testProject.repositories;

import com.example.testProject.models.UserDataRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserDataRequest, Long> {
    @Query(value = "SELECT * FROM users where name = ?1", nativeQuery = true)
    Optional<UserDataRequest> findByName(String name);
}
