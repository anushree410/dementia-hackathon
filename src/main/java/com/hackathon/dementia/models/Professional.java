package com.hackathon.dementia.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Professional")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Professional {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String speciality;
    private String working_days;
    private String working_slots;
}
