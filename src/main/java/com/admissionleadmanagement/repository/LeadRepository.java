package com.admissionleadmanagement.repository;

import com.admissionleadmanagement.entity.Lead;
import com.admissionleadmanagement.enums.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    List<Lead> findAllByOrderByCreatedAtDesc();

    List<Lead> findByStatusOrderByCreatedAtDesc(LeadStatus status);

    List<Lead> findByAssignedCounselorId(Long counselorId);

    List<Lead> findByNextFollowUpDateBeforeAndStatusNot(LocalDate date, LeadStatus status);

    long countByStatus(LeadStatus status);

    long countByConverted(boolean converted);
}
