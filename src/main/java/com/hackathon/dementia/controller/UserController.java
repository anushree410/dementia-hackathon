package com.hackathon.dementia.controller;

import com.hackathon.dementia.models.*;
import com.hackathon.dementia.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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
            parsedDay.set(LocalDate.parse((String) scheduleData.get("scheduleday"), dateFormatter));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse((String) scheduleData.get("starttime"), timeFormatter);
        LocalDateTime endTime = LocalDateTime.parse((String) scheduleData.get("endtime"), timeFormatter);

        Schedule newSchedule = new Schedule();
        newSchedule.setStarttime(startTime);
        newSchedule.setEndtime(endTime);
        newSchedule.setScheduleday(parsedDay.get().toString()); // Convert LocalDate back to String if needed
        newSchedule.setDescription((String) scheduleData.get("description"));
        newSchedule.setPatient(patientOptional.get());
        newSchedule.setProfessional(professionalOptional.get());

        Schedule savedSchedule = scheduleRepo.save(newSchedule);

        return new ResponseEntity<>(savedSchedule, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/getschedules")
    public ResponseEntity<List<Schedule>> getSchedulesByUserId(@PathVariable Long userId) {
        logger.info("Fetching schedules for user ID: {}", userId);
        try {
            Optional<Patient> optionalPatient = patientRepo.findById(userId);
            if (!optionalPatient.isPresent()) {
                logger.warn("No patient found with ID: {}", userId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<Schedule> schedules = scheduleRepo.findByPatientId(optionalPatient.get().getId());
            logger.info("Found {} schedules for patient ID: {}", schedules.size(), userId);
            return new ResponseEntity<>(schedules, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching schedules for user ID: {}. Error: {}", userId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
