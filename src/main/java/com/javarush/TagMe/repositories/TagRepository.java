package com.javarush.TagMe.repositories;

import com.javarush.TagMe.model.Tag;
import com.javarush.TagMe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> {
    @Query("SELECT DISTINCT t.tag FROM Tag t")
    public List<Tag> findDistinctTags();
}
