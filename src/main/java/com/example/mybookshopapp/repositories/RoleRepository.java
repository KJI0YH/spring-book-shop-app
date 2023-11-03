package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findRoleEntityByNameIgnoreCase(String name);
}
