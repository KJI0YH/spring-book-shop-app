package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.ApproveContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApproveContactRepository extends JpaRepository<ApproveContactEntity, Integer> {
    ApproveContactEntity findApproveContactEntityByContactAndApprovedIsFalse(String contact);
    ApproveContactEntity findApproveContactEntityByContact(String contact);
}
