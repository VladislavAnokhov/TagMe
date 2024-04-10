package com.javarush.TagMe.repositories;

import com.javarush.TagMe.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Integer> {
    void deleteById(int id);
}
