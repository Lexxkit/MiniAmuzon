package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    private String city;
    private LocalDateTime regDate;
    private String password;
    private boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Avatar avatar;

    @OneToMany(mappedBy = "author")
    private List<Ads> adsList;


}
