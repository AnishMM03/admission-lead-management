package com.admissionleadmanagement.repository;

import com.admissionleadmanagement.entity.LeadSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeadSourceRepository extends JpaRepository<LeadSource, Long> {
    Optional<LeadSource> findByName(String name);
}
