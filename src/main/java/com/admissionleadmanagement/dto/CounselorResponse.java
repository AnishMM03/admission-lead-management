package com.admissionleadmanagement.dto;

public record CounselorResponse(
        Long id,
        String name,
        String phone,
        String email,
        String department,
        boolean active
) {
}
