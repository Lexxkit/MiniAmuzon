package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Avatar")
public class Avatar {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(mappedBy = "avatar")
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private byte[] image;



}
