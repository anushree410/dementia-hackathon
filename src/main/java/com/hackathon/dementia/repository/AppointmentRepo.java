package com.hackathon.dementia.repository;

import com.hackathon.dementia.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
}
