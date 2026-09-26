package com.admissionleadmanagement.repository;

import com.admissionleadmanagement.entity.FollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    List<FollowUp> findByLeadIdOrderByCreatedAtDesc(Long leadId);
}
