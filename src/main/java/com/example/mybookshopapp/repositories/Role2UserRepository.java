package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.Role2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Role2UserRepository extends JpaRepository<Role2UserEntity, Integer> {
    int countAllByRoleId(Integer roleId);
}
