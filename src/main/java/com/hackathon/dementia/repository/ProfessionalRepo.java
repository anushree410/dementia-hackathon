package com.hackathon.dementia.repository;

import com.hackathon.dementia.models.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalRepo extends JpaRepository<Professional, Long> {
}
