package com.project.shopping_site.Repositories;

import com.project.shopping_site.Entities.Comment;
import com.project.shopping_site.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Calendar;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Transactional
    @Modifying
    @Query("delete from Comment c where c.id = :id and c.userId = :userId")
    int deleteByIdAndUserId(@Param("id") Integer id, @Param("userId") User userId);
    @Transactional
    @Modifying
    @Query("update Comment c set c.message = :message, c.updatedAt = :timeStamp where (c.id = :id and c.userId = :user)")
    int updateByIdAndUserId(@Param("message") String message, @Param("timeStamp")Calendar timeStamp, @Param("id") Integer id, @Param("user") User user);
}
