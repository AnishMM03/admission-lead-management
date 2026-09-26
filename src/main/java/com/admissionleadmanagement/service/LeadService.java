package com.admissionleadmanagement.service;

import com.admissionleadmanagement.dto.*;
import com.admissionleadmanagement.entity.Counselor;
import com.admissionleadmanagement.entity.FollowUp;
import com.admissionleadmanagement.entity.Lead;
import com.admissionleadmanagement.entity.LeadSource;
import com.admissionleadmanagement.enums.FollowUpStatus;
import com.admissionleadmanagement.enums.LeadStatus;
import com.admissionleadmanagement.repository.CounselorRepository;
import com.admissionleadmanagement.repository.FollowUpRepository;
import com.admissionleadmanagement.repository.LeadRepository;
import com.admissionleadmanagement.repository.LeadSourceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final LeadSourceRepository leadSourceRepository;
    private final CounselorRepository counselorRepository;
    private final FollowUpRepository followUpRepository;

    public LeadService(
            LeadRepository leadRepository,
            LeadSourceRepository leadSourceRepository,
            CounselorRepository counselorRepository,
            FollowUpRepository followUpRepository
    ) {
        this.leadRepository = leadRepository;
        this.leadSourceRepository = leadSourceRepository;
        this.counselorRepository = counselorRepository;
        this.followUpRepository = followUpRepository;
    }

    public List<LeadResponse> getAllLeads() {
        return leadRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LeadResponse getLeadById(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lead not found with id: " + id));
        return toResponse(lead);
    }

    @Transactional
    public LeadResponse createLead(CreateLeadRequest request) {
        LeadSource source = leadSourceRepository.findByName(request.sourceName())
                .orElseGet(() -> leadSourceRepository.save(
                        LeadSource.builder()
                                .name(request.sourceName())
                                .channel(request.sourceChannel() != null ? request.sourceChannel() : "Unknown")
                                .description("Created automatically")
                                .build()
                ));

        Counselor counselor = null;
        if (request.assignedCounselorId() != null) {
            counselor = counselorRepository.findById(request.assignedCounselorId())
                    .orElseThrow(() -> new EntityNotFoundException("Counselor not found"));
        }

        Lead lead = Lead.builder()
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
                .city(request.city())
                .address(request.address())
                .source(source)
                .assignedCounselor(counselor)
                .coursePreferences(request.coursePreferences() == null ? new ArrayList<>() : request.coursePreferences())
                .status(LeadStatus.NEW)
                .leadScore(10)
                .nextFollowUpDate(LocalDate.now().plusDays(2))
                .build();

        Lead saved = leadRepository.save(lead);

        return toResponse(saved);
    }

    @Transactional
    public LeadResponse assignCounselor(Long leadId, Long counselorId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new EntityNotFoundException("Lead not found with id: " + leadId));

        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new EntityNotFoundException("Counselor not found with id: " + counselorId));

        lead.setAssignedCounselor(counselor);
        lead.setStatus(LeadStatus.CONTACTED);
        lead.setLeadScore(Math.max(lead.getLeadScore(), 35));

        return toResponse(leadRepository.save(lead));
    }

    @Transactional
    public LeadResponse updateLeadStatus(Long leadId, UpdateLeadStatusRequest request) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new EntityNotFoundException("Lead not found with id: " + leadId));

        lead.setStatus(request.status());

        if (request.status() == LeadStatus.CONVERTED) {
            lead.setConverted(true);
            lead.setLeadScore(100);
        }

        if (request.status() == LeadStatus.QUALIFIED || request.status() == LeadStatus.HOT) {
            lead.setLeadScore(Math.max(lead.getLeadScore(), 60));
        }

        return toResponse(leadRepository.save(lead));
    }

    @Transactional
    public FollowUpResponse createFollowUp(Long leadId, CreateFollowUpRequest request) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new EntityNotFoundException("Lead not found with id: " + leadId));

        FollowUp followUp = FollowUp.builder()
                .lead(lead)
                .type(request.type())
                .status(request.status())
                .notes(request.notes())
                .scheduledAt(request.scheduledAt() != null ? request.scheduledAt() : LocalDateTime.now().plusDays(1))
                .completedAt(request.status() == FollowUpStatus.COMPLETED ? LocalDateTime.now() : null)
                .build();

        if (request.status() == FollowUpStatus.COMPLETED) {
            lead.setStatus(LeadStatus.CONTACTED);
            lead.setNextFollowUpDate(LocalDate.now().plusDays(4));
        } else if (request.status() == FollowUpStatus.PENDING) {
            lead.setStatus(LeadStatus.FOLLOW_UP_SCHEDULED);
            lead.setNextFollowUpDate(request.scheduledAt() != null ? request.scheduledAt().toLocalDate() : LocalDate.now().plusDays(2));
        }

        leadRepository.save(lead);
        FollowUp saved = followUpRepository.save(followUp);

        return toResponse(saved);
    }

    public List<FollowUpResponse> getFollowUps(Long leadId) {
        return followUpRepository.findByLeadIdOrderByCreatedAtDesc(leadId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CounselorResponse> getAllCounselors() {
        return counselorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DashboardResponse getDashboard() {
        long totalLeads = leadRepository.count();
        long newLeads = leadRepository.countByStatus(LeadStatus.NEW);
        long contactedLeads = leadRepository.countByStatus(LeadStatus.CONTACTED);
        long qualifiedLeads = leadRepository.countByStatus(LeadStatus.QUALIFIED);
        long hotLeads = leadRepository.countByStatus(LeadStatus.HOT);
        long convertedLeads = leadRepository.countByConverted(true);

        LocalDate today = LocalDate.now();
        long overdueFollowUps = leadRepository.findByNextFollowUpDateBeforeAndStatusNot(today, LeadStatus.CONVERTED)
                .size();

        return new DashboardResponse(
                totalLeads,
                newLeads,
                contactedLeads,
                qualifiedLeads,
                hotLeads,
                convertedLeads,
                overdueFollowUps
        );
    }

    private LeadResponse toResponse(Lead lead) {
        return new LeadResponse(
                lead.getId(),
                lead.getFullName(),
                lead.getEmail(),
                lead.getPhone(),
                lead.getCity(),
                lead.getSource() != null ? lead.getSource().getName() : "N/A",
                lead.getAssignedCounselor() != null ? lead.getAssignedCounselor().getName() : "Unassigned",
                lead.getCoursePreferences(),
                lead.getStatus(),
                lead.getLeadScore(),
                lead.isConverted(),
                lead.getNextFollowUpDate(),
                lead.getCreatedAt(),
                lead.getUpdatedAt()
        );
    }

    private CounselorResponse toResponse(Counselor counselor) {
        return new CounselorResponse(
                counselor.getId(),
                counselor.getName(),
                counselor.getPhone(),
                counselor.getEmail(),
                counselor.getDepartment(),
                counselor.isActive()
        );
    }

    private FollowUpResponse toResponse(FollowUp followUp) {
        return new FollowUpResponse(
                followUp.getId(),
                followUp.getType(),
                followUp.getStatus(),
                followUp.getNotes(),
                followUp.getScheduledAt(),
                followUp.getCompletedAt(),
                followUp.getCreatedAt()
        );
    }
}
