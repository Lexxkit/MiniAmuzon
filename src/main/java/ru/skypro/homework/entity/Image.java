package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer fileSize;
    private String mediaType;
    @Lob
    private byte[] data;
    @ManyToOne
    @JoinColumn(name = "ads_id")
    private Ads ads;

}
