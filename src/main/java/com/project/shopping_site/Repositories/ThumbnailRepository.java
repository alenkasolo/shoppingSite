package com.project.shopping_site.Repositories;

import com.project.shopping_site.Entities.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, Integer> {
}
