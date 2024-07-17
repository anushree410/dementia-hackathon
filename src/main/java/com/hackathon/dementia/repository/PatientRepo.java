package com.hackathon.dementia.repository;

import com.hackathon.dementia.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {
//    List<Patient> findByStatus(String status);
//    Patient findPatientById(Long id);
//        return patientRepository.findById(id)
//                .orElse(null); // Return null if patient not found
//    }
}
