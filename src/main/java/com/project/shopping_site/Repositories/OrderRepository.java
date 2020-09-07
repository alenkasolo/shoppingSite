package com.project.shopping_site.Repositories;

import com.project.shopping_site.Entities.Order;
import com.project.shopping_site.Entities.Product;
import com.project.shopping_site.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("select o from Order o where (o.product.id = :product and o.user = :user and o.paid = false)")
    Order findUnpaidOrderByProductAndUser(@Param("product")Integer product, @Param("user") User user);
    @Query("select o from Order o where (o.user = :user and o.paid = false) order by o.id desc ")
    List<Order> findUnpaidOrdersByUserInDesc(@Param("user") User user);
    Order findByIdAndUser(Integer id, User user);
}
