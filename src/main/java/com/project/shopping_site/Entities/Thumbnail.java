package com.project.shopping_site.Entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "thumbnail")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Thumbnail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ProductImage image;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}
