package com.admissionleadmanagement.dto;

public record DashboardResponse(
        long totalLeads,
        long newLeads,
        long contactedLeads,
        long qualifiedLeads,
        long hotLeads,
        long convertedLeads,
        long overdueFollowUps
) {
}
