package com.admissionleadmanagement.dto;

public record LeadSourceResponse(
        Long id,
        String name,
        String channel,
        String description
) {
}
