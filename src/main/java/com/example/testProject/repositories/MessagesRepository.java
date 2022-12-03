package com.example.testProject.repositories;

import com.example.testProject.models.MessageRequest;
import com.example.testProject.models.UserDataRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessagesRepository extends JpaRepository<MessageRequest, Long> {
    @Query(value = "SELECT * FROM messages where name = ?1 order by updated_date DESC limit 10", nativeQuery = true)
    List<MessageRequest> getLastTen(String name);
}
