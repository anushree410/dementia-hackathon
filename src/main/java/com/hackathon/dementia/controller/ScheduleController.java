package com.hackathon.dementia.controller;

import com.hackathon.dementia.models.Professional;
import com.hackathon.dementia.models.Schedule;
import com.hackathon.dementia.repository.ProfessionalRepo;
import com.hackathon.dementia.repository.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

public class ScheduleController {
    @Autowired
    private ScheduleRepo scheduleRepo;

    @GetMapping("/getAll")
    public ResponseEntity<List<Schedule>> getAllProfessional(){
        try{
            List<Schedule> schedulelist = new ArrayList<>();
            scheduleRepo.findAll().forEach(schedulelist::add);
            return new ResponseEntity<>(schedulelist, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
