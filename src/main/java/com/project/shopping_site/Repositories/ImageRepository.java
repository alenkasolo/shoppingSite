package com.project.shopping_site.Repositories;

import com.project.shopping_site.Entities.Product;
import com.project.shopping_site.Entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findAllByProduct(Product name);
}
