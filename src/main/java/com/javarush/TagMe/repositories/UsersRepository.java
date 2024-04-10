package com.javarush.TagMe.repositories;

import com.javarush.TagMe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<User,Integer> {

    @Query(value = "SELECT * FROM users ORDER BY RANDOM()", nativeQuery = true)
    List<User> findAllRandom();
    @Query("SELECT u FROM User u WHERE u.gender = :gender")
    Page<User> findByGender(String gender, Pageable pageable);
    @Query("SELECT u FROM User u JOIN u.tagsList t WHERE t.tag = :tag")
    Page<User> findByTag(@Param("tag") String tag, Pageable pageable);
    @Query("SELECT DISTINCT t.tag FROM Tag t")
    List<String> findDistinctTags();
    @Query(value = "SELECT * FROM users u WHERE u.gender = :gender AND EXISTS (SELECT 1 FROM tag t WHERE t.user_id = u.id AND t.tag = :tag)", nativeQuery = true)
    Page<User> findByGenderAndTag(@Param("gender") String gender, @Param("tag") String tag,Pageable pageable);
    User findByEmail(String email);
}
