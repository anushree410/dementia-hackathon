package com.hackathon.dementia.repository;

import com.hackathon.dementia.models.Carer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarerRepo extends JpaRepository<Carer, Long> {
}
