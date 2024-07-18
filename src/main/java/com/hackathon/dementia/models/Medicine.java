package com.hackathon.dementia.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Medicines")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String freq;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
}
