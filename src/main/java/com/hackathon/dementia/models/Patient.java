package com.hackathon.dementia.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Carer> caretakers;

}
