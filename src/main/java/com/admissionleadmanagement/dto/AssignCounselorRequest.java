package com.admissionleadmanagement.dto;

import jakarta.validation.constraints.NotNull;

public record AssignCounselorRequest(@NotNull Long counselorId) {
}
