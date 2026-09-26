package com.admissionleadmanagement.controller;

import com.admissionleadmanagement.dto.*;
import com.admissionleadmanagement.enums.LeadStatus;
import com.admissionleadmanagement.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {
        return leadService.getDashboard();
    }

    @GetMapping("/leads")
    public List<LeadResponse> getAllLeads() {
        return leadService.getAllLeads();
    }

    @GetMapping("/leads/{id}")
    public LeadResponse getLeadById(@PathVariable Long id) {
        return leadService.getLeadById(id);
    }

    @PostMapping("/leads")
    public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody CreateLeadRequest request) {
        return new ResponseEntity<>(leadService.createLead(request), HttpStatus.CREATED);
    }

    @PutMapping("/leads/{id}/status")
    public LeadResponse updateLeadStatus(@PathVariable Long id, @Valid @RequestBody UpdateLeadStatusRequest request) {
        return leadService.updateLeadStatus(id, request);
    }

    @PutMapping("/leads/{id}/assign")
    public LeadResponse assignCounselor(@PathVariable Long id, @Valid @RequestBody AssignCounselorRequest request) {
        return leadService.assignCounselor(id, request.counselorId());
    }

    @GetMapping("/leads/{id}/followups")
    public List<FollowUpResponse> getFollowUps(@PathVariable Long id) {
        return leadService.getFollowUps(id);
    }

    @PostMapping("/leads/{id}/followups")
    public ResponseEntity<FollowUpResponse> createFollowUp(
            @PathVariable Long id,
            @Valid @RequestBody CreateFollowUpRequest request
    ) {
        return new ResponseEntity<>(leadService.createFollowUp(id, request), HttpStatus.CREATED);
    }

    @GetMapping("/counselors")
    public List<CounselorResponse> getCounselors() {
        return leadService.getAllCounselors();
    }

    @GetMapping("/leads/status/{status}")
    public List<LeadResponse> getLeadsByStatus(@PathVariable LeadStatus status) {
        return leadService.getAllLeads()
                .stream()
                .filter(lead -> lead.status() == status)
                .toList();
    }
}
