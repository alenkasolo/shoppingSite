package com.project.shopping_site.Repositories;

import com.project.shopping_site.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);
}
