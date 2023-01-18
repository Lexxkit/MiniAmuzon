package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "ads")
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal price;
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    @OneToMany(mappedBy = "ads")
    private List<Comment> comments;
    @OneToMany(mappedBy = "ads")
    private List<Image> images;
}
