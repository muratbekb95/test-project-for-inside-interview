package com.example.testProject.models;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
@Table(name = "messages")
public class MessageRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    @Setter(AccessLevel.PRIVATE)
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    @Setter(AccessLevel.PRIVATE)
    private ZonedDateTime updatedDate;

    @PrePersist
    private void onSave() {
        this.createdDate = ZonedDateTime.now();
        this.updatedDate = ZonedDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedDate = ZonedDateTime.now();
    }

    @Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserDataRequest user;

    @Column(name = "name")
    private String name;

    @Column(name = "message")
    private String message;

    public MessageRequest(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
