package com.hackathon.dementia.repository;

import com.hackathon.dementia.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s WHERE s.patient.id = :patientId")
    List<Schedule> findByPatientId(@Param("patientId") Long patientId);
}
