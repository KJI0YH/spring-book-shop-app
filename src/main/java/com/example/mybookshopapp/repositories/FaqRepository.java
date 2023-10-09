package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, Integer> {
    List<FaqEntity> findAllByOrderBySortIndex();
}
