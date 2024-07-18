package com.hackathon.dementia.repository;

import com.hackathon.dementia.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    @Query("SELECT s FROM Notification s WHERE s.patient.id = :patientId")
    List<Notification> findByPatientId(@Param("patientId") Long patientId);
}
