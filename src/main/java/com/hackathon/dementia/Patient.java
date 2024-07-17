package com.hackathon.dementia;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Patients")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private long phone;
    private String address;
    private int age;
    private String gender;
//    private image

}
