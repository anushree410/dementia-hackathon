package com.hackathon.dementia.controller;

import com.hackathon.dementia.models.Professional;
import com.hackathon.dementia.repository.ProfessionalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/professional")
public class ProfessionalController {
    @Autowired
    private ProfessionalRepo professionalRepo;

    @GetMapping("/getAll")
    public ResponseEntity<List<Professional>> getAllProfessional(){
        try{
            List<Professional> professionallist = new ArrayList<>();
            professionalRepo.findAll().forEach(professionallist::add);
            return new ResponseEntity<>(professionallist, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("getById/{id}")
    public ResponseEntity<Professional> getProfessionalById(@PathVariable Long id) {
        Optional<Professional> optionalProfessional = professionalRepo.findById(id);
        if (optionalProfessional.isPresent()) {
            return new ResponseEntity<>(optionalProfessional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Professional> addProfessional(@RequestBody Professional t){
        Professional tObj = professionalRepo.save(t);
        return new ResponseEntity<>(tObj, HttpStatus.OK);
    }

}
