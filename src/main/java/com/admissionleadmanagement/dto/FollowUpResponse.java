package com.admissionleadmanagement.dto;

import com.admissionleadmanagement.enums.FollowUpStatus;
import com.admissionleadmanagement.enums.FollowUpType;

import java.time.LocalDateTime;

public record FollowUpResponse(
        Long id,
        FollowUpType type,
        FollowUpStatus status,
        String notes,
        LocalDateTime scheduledAt,
        LocalDateTime completedAt,
        LocalDateTime createdAt
) {
}
