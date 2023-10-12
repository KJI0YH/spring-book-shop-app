package com.example.mybookshopapp.security;

import com.example.mybookshopapp.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findUserEntityById(Integer id);

    UserEntity findByHash(String hash);

    UserEntity findUserEntityByName(String name);

    UserEntity findUserEntityByEmail(String email);

    UserEntity findUserEntityByPhone(String phone);
}
