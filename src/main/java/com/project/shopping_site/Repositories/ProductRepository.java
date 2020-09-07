package com.project.shopping_site.Repositories;

import com.project.shopping_site.Entities.Category;
import com.project.shopping_site.Entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    String QUERY = "select * from product"
            + " where name ilike CONCAT('%',?1, '%')";
    Product findByName(String name);
    @Query("select p from Product p where p.categoryId = :category order by p.id desc")
    List<Product> findByCategoryIdInDesc(@Param("category") Category category, Pageable pageable);
    @Query(value = "SELECT COUNT(*) FROM (SELECT id FROM product where category_id = ?1 LIMIT 200) sub;", nativeQuery = true)
    Double countProducts(Integer categoryId);
    @Query(value = QUERY, nativeQuery = true)
    List<Product> searchedProducts(String name);
}

