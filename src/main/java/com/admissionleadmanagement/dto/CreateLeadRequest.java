package com.admissionleadmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateLeadRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank String phone,
        String city,
        String address,
        @NotBlank String sourceName,
        String sourceChannel,
        List<String> coursePreferences,
        Long assignedCounselorId
) {
}
