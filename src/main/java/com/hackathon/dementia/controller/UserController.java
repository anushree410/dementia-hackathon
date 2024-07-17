package com.hackathon.dementia.controller;

import com.hackathon.dementia.models.Carer;
import com.hackathon.dementia.repository.CarerRepo;
import com.hackathon.dementia.repository.PatientRepo;
import com.hackathon.dementia.models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private CarerRepo carerRepo;

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

}
