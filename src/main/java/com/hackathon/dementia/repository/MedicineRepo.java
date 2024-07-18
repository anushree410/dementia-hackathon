package com.hackathon.dementia.repository;

import com.hackathon.dementia.models.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepo extends JpaRepository<Medicine, Long> {
    @Query("SELECT s FROM Medicine s WHERE s.patient.id = :patientId")
    List<Medicine> findByPatientId(@Param("patientId") Long patientId);
}