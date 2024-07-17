package com.hackathon.dementia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepo extends JpaRepository<Patient, String> {
//    List<Patient> findByStatus(String status);
//    Patient findPatientById(Long id);
//        return patientRepository.findById(id)
//                .orElse(null); // Return null if patient not found
//    }
}
