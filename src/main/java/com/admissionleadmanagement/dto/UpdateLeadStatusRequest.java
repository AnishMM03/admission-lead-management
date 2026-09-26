package com.admissionleadmanagement.dto;

import com.admissionleadmanagement.enums.LeadStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateLeadStatusRequest(@NotNull LeadStatus status) {
}
