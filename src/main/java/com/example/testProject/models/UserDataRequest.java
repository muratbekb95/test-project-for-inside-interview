package com.example.testProject.models;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = {"messages"})
@ToString(exclude = {"messages"})
@Table(name = "users")
public class UserDataRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Transient
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MessageRequest> messages;

    public UserDataRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
