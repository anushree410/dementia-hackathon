package com.hackathon.dementia.controller;

import com.hackathon.dementia.models.Carer;
import com.hackathon.dementia.models.Professional;
import com.hackathon.dementia.models.Schedule;
import com.hackathon.dementia.repository.CarerRepo;
import com.hackathon.dementia.repository.PatientRepo;
import com.hackathon.dementia.models.Patient;
import com.hackathon.dementia.repository.ProfessionalRepo;
import com.hackathon.dementia.repository.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private CarerRepo carerRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private ProfessionalRepo  professionalRepo;

    @GetMapping("/getAll")
    public ResponseEntity<List<Patient>> getAllPatient(){
        try{
            List<Patient> patientlist = new ArrayList<>();
            patientRepo.findAll().forEach(patientlist::add);
            return new ResponseEntity<>(patientlist, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("getById/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> optionalPatient = patientRepo.findById(id);
        if (optionalPatient.isPresent()) {
            return new ResponseEntity<>(optionalPatient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Patient> addPatient(@RequestBody Patient t){
        Patient tObj = patientRepo.save(t);
        return new ResponseEntity<>(tObj, HttpStatus.OK);
    }

    @PostMapping("updatePatientById/{id}")
    public ResponseEntity<Patient> updatePatientById(@PathVariable String id, @RequestBody Patient newPatientData){
        Optional<Patient> oldpatient = patientRepo.findById(Long.valueOf(id));
        if (oldpatient.isPresent()){
            Patient updated = oldpatient.get();
            updated.setEmail(newPatientData.getEmail());
            Patient tObj = patientRepo.save(updated);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userId}/addCaretaker")
    public ResponseEntity<String> addCaretakerToUser(
            @PathVariable Long userId,
            @RequestBody Carer caretakerDetails) {

        Optional<Patient> patientOptional = patientRepo.findById(userId);
        if (!patientOptional.isPresent()) {
            return new ResponseEntity<>("Patient not found.", HttpStatus.NOT_FOUND);
        }

        Carer caretaker = new Carer();
        caretaker.setName(caretakerDetails.getName());
        caretaker.setEmail(caretakerDetails.getEmail());
        caretaker.setPhone(caretakerDetails.getPhone());
        caretaker.setAddress(caretakerDetails.getAddress());
        caretaker.setAge(caretakerDetails.getAge());
        caretaker.setGender(caretakerDetails.getGender());
        caretaker.setPatient(patientOptional.get()); // Associate the caretaker with the patient

        Carer savedCaretaker = carerRepo.save(caretaker);
        return new ResponseEntity<>("Added successfully.", HttpStatus.OK);
    }

    @PostMapping("/{userId}/addschedule")
    public ResponseEntity<Schedule> addSchedule(@PathVariable Long userId, @RequestBody Map<String, Object> scheduleData) {
        Optional<Patient> patientOptional = patientRepo.findById(userId);
        if (!patientOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long professionalId = Long.valueOf(scheduleData.get("professional_id").toString());
        Optional<Professional> professionalOptional = professionalRepo.findById(professionalId);
        if (!professionalOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        AtomicReference<LocalDate> parsedDay = new AtomicReference<>();
        try {
            parsedDay.set(LocalDate.parse((String) scheduleData.get("day"), dateFormatter));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse((String) scheduleData.get("startTime"), timeFormatter);
        LocalDateTime endTime = LocalDateTime.parse((String) scheduleData.get("endTime"), timeFormatter);

        Schedule newSchedule = new Schedule(
                startTime,
                endTime,
                parsedDay.get().toString(), // Convert LocalDate back to String if needed
                (String) scheduleData.get("description"),
                patientOptional.get(),
                professionalOptional.get()
        );

        Schedule savedSchedule = scheduleRepo.save(newSchedule);

        return new ResponseEntity<>(savedSchedule, HttpStatus.CREATED);
    }


}
