package com.hackathon.dementia.controller;

import com.hackathon.dementia.models.Carer;
import com.hackathon.dementia.repository.CarerRepo;
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
}
