package com.hackathon.dementia.controller;

import com.hackathon.dementia.models.Carer;
import com.hackathon.dementia.models.Patient;
import com.hackathon.dementia.repository.CarerRepo;
import com.hackathon.dementia.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/caretaker")
public class CaretakerController {
    @Autowired
    private CarerRepo carerRepo;

    @Autowired
    private PatientRepo patientRepo;

    @GetMapping("/getAll")
    public ResponseEntity<List<Carer>> getAllCarer(){
        try{
            List<Carer> carerlist = new ArrayList<>();
            carerRepo.findAll().forEach(carerlist::add);
            return new ResponseEntity<>(carerlist, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<Carer> getCarerById(@PathVariable Long id) {
        Optional<Carer> optionalCarer = carerRepo.findById(id);
        if (optionalCarer.isPresent()) {
            return new ResponseEntity<>(optionalCarer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Carer> addCarer(@RequestBody Carer t){
        Carer tObj = carerRepo.save(t);
        return new ResponseEntity<>(tObj, HttpStatus.OK);
    }
    // Not possible since caretaker-patient_id field not_nullaable
    @PutMapping("/{id}/updatePatient/{patientId}")
    public ResponseEntity<?> updateCarerWithPatient(@PathVariable Long id, @PathVariable Long patientId, @RequestBody Carer carerDetails) {
        Optional<Carer> optionalCarer = carerRepo.findById(id);

        if (!optionalCarer.isPresent()) {
            return new ResponseEntity<>("Carer not found.", HttpStatus.NOT_FOUND);
        }

        Carer existingCarer = optionalCarer.get();

        // Update fields of the existing carer object with details from the request body
        existingCarer.setName(carerDetails.getName());
        existingCarer.setEmail(carerDetails.getEmail());
        existingCarer.setPhone(carerDetails.getPhone());
        existingCarer.setAddress(carerDetails.getAddress());
        existingCarer.setAge(carerDetails.getAge());
        existingCarer.setGender(carerDetails.getGender());

        // Fetch the patient entity using the patient ID from path variable
        Optional<Patient> optionalPatient = patientRepo.findById(patientId);
        if (!optionalPatient.isPresent()) {
            return new ResponseEntity<>("Patient not found.", HttpStatus.BAD_REQUEST);
        }

        // Associate the carer with the patient
        existingCarer.setPatient(optionalPatient.get());

        // Save the updated carer entity
        Carer updatedCarer = carerRepo.save(existingCarer);

        return new ResponseEntity<>(updatedCarer, HttpStatus.OK);
    }



}
