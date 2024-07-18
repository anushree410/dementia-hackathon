package com.hackathon.dementia.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "Appointment")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime appointmentTime;
    private Duration duration;
    private String description;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;
}
