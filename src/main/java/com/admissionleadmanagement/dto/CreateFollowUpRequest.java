package com.admissionleadmanagement.dto;

import com.admissionleadmanagement.enums.FollowUpStatus;
import com.admissionleadmanagement.enums.FollowUpType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateFollowUpRequest(
        @NotNull FollowUpType type,
        @NotNull FollowUpStatus status,
        String notes,
        LocalDateTime scheduledAt
) {
}
