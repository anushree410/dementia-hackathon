package com.hackathon.dementia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/patients")
public class Controller {

    @Autowired
    private PatientRepo patientRepo;

    @GetMapping("/getAllPatients")
    public ResponseEntity<List<Patient>> getAllTask(){
        try{
            List<Patient> patientlist = new ArrayList<>();
            patientRepo.findAll().forEach(patientlist::add);
            return new ResponseEntity<>(patientlist, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @GetMapping("/{id}")
//    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
//        Optional<Patient> optionalPatient = patientRepo.findById(id);
//        if (optionalPatient.isPresent()) {
//            return new ResponseEntity<>(optionalPatient.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//    @GetMapping("/getByStatus/{status}")
//    public ResponseEntity<List<Patient>> getTaskByStatus(@PathVariable String status){
//        List<Patient> taskData = patientRepo.findByStatus(status);
//        return new ResponseEntity<>(taskData, HttpStatus.OK);
//    }

    @PostMapping("/add")
    public ResponseEntity<Patient> addTask(@RequestBody Patient t){
        Patient tObj = patientRepo.save(t);
        return new ResponseEntity<>(tObj, HttpStatus.OK);
    }

    @PostMapping("updatePatientById/{id}")
    public ResponseEntity<Patient> updateTaskById(@PathVariable String id, @RequestBody Patient newPatientData){
        Optional<Patient> oldpatient = patientRepo.findById(id);
        if (oldpatient.isPresent()){
            Patient updated = oldpatient.get();
            updated.setEmail(newPatientData.getEmail());
            Patient tObj = patientRepo.save(updated);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
