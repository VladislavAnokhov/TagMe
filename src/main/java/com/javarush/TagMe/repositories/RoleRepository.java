package com.javarush.TagMe.repositories;

import com.javarush.TagMe.model.Role;
import com.javarush.TagMe.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);
}
