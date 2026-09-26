package com.admissionleadmanagement.config;

import com.admissionleadmanagement.entity.Counselor;
import com.admissionleadmanagement.entity.Lead;
import com.admissionleadmanagement.entity.LeadSource;
import com.admissionleadmanagement.enums.LeadStatus;
import com.admissionleadmanagement.repository.CounselorRepository;
import com.admissionleadmanagement.repository.LeadRepository;
import com.admissionleadmanagement.repository.LeadSourceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CounselorRepository counselorRepository;
    private final LeadSourceRepository leadSourceRepository;
    private final LeadRepository leadRepository;

    public DataSeeder(
            CounselorRepository counselorRepository,
            LeadSourceRepository leadSourceRepository,
            LeadRepository leadRepository
    ) {
        this.counselorRepository = counselorRepository;
        this.leadSourceRepository = leadSourceRepository;
        this.leadRepository = leadRepository;
    }

    @Override
    public void run(String... args) {
        // Seed Counselors
        if (counselorRepository.count() == 0) {
            counselorRepository.save(Counselor.builder()
                    .name("Riya Sharma")
                    .phone("9876543210")
                    .email("riya@college.com")
                    .department("Admissions")
                    .active(true)
                    .build());

            counselorRepository.save(Counselor.builder()
                    .name("Arjun Mehta")
                    .phone("9988776655")
                    .email("arjun@college.com")
                    .department("Admissions")
                    .active(true)
                    .build());

            counselorRepository.save(Counselor.builder()
                    .name("Priya Kapoor")
                    .phone("8765432109")
                    .email("priya@college.com")
                    .department("Student Services")
                    .active(true)
                    .build());
        }

        // Seed Lead Sources
        if (leadSourceRepository.count() == 0) {
            leadSourceRepository.save(LeadSource.builder()
                    .name("Website")
                    .channel("Website")
                    .description("Web inquiries")
                    .build());

            leadSourceRepository.save(LeadSource.builder()
                    .name("WhatsApp")
                    .channel("WhatsApp")
                    .description("WhatsApp messages")
                    .build());

            leadSourceRepository.save(LeadSource.builder()
                    .name("Walk-in")
                    .channel("Walk-in")
                    .description("Physical campus inquiries")
                    .build());

            leadSourceRepository.save(LeadSource.builder()
                    .name("Fair")
                    .channel("Fair")
                    .description("Education fair")
                    .build());

            leadSourceRepository.save(LeadSource.builder()
                    .name("Phone")
                    .channel("Phone")
                    .description("Phone call inquiries")
                    .build());
        }

        // Seed Sample Leads
        if (leadRepository.count() == 0) {
            Counselor counselor1 = counselorRepository.findById(1L).orElse(null);
            Counselor counselor2 = counselorRepository.findById(2L).orElse(null);
            LeadSource website = leadSourceRepository.findByName("Website").orElse(null);

            leadRepository.save(Lead.builder()
                    .fullName("Aisha Khan")
                    .email("aisha@example.com")
                    .phone("9876543210")
                    .city("Bengaluru")
                    .address("MG Road")
                    .source(website)
                    .assignedCounselor(counselor1)
                    .coursePreferences(Arrays.asList("B.Tech", "BBA"))
                    .status(LeadStatus.CONTACTED)
                    .leadScore(45)
                    .nextFollowUpDate(LocalDate.now().plusDays(2))
                    .build());

            leadRepository.save(Lead.builder()
                    .fullName("Raj Patel")
                    .email("raj@example.com")
                    .phone("9123456789")
                    .city("Mumbai")
                    .address("Bandra")
                    .source(website)
                    .assignedCounselor(counselor2)
                    .coursePreferences(Arrays.asList("B.Tech"))
                    .status(LeadStatus.QUALIFIED)
                    .leadScore(70)
                    .nextFollowUpDate(LocalDate.now().minusDays(1))
                    .build());

            leadRepository.save(Lead.builder()
                    .fullName("Priya Singh")
                    .email("priya@example.com")
                    .phone("8765432101")
                    .city("Delhi")
                    .address("South Delhi")
                    .source(website)
                    .assignedCounselor(counselor1)
                    .coursePreferences(Arrays.asList("MBA"))
                    .status(LeadStatus.HOT)
                    .leadScore(85)
                    .converted(false)
                    .nextFollowUpDate(LocalDate.now().plusDays(1))
                    .build());

            leadRepository.save(Lead.builder()
                    .fullName("Vikram Gupta")
                    .email("vikram@example.com")
                    .phone("7654321098")
                    .city("Pune")
                    .address("Hinjewadi")
                    .source(website)
                    .status(LeadStatus.NEW)
                    .leadScore(15)
                    .nextFollowUpDate(LocalDate.now().plusDays(3))
                    .build());
        }
    }
}
