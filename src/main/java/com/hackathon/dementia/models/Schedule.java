package com.hackathon.dementia.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="Schedule")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String day; // Assuming day is represented as a string, e.g., "Monday"
    private String description;
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "professinal_id", nullable = false)
    private Professional professional;
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "schedule_medicine",
//            joinColumns = @JoinColumn(name = "schedule_id"),
//            inverseJoinColumns = @JoinColumn(name = "medicine_id"))
//    private Set<Medicine> medicines;
    public Schedule(LocalDateTime startTime, LocalDateTime endTime, String day, String description, Patient patient, Professional professional) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.description = description;
        this.patient = patient;
        this.professional = professional;
    }
}
