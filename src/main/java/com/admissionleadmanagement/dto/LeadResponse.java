package com.admissionleadmanagement.dto;

import com.admissionleadmanagement.enums.LeadStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record LeadResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String city,
        String sourceName,
        String assignedCounselorName,
        List<String> coursePreferences,
        LeadStatus status,
        int leadScore,
        boolean converted,
        LocalDate nextFollowUpDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
