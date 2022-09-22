package com.ironhack.ironbank.service;

import com.ironhack.ironbank.model.StudentChecking;
import com.ironhack.ironbank.repository.StudentCheckingRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StudentCheckingService {

    final
    StudentCheckingRepository studentCheckingRepository;

    public StudentCheckingService(StudentCheckingRepository studentCheckingRepository) {
        this.studentCheckingRepository = studentCheckingRepository;
    }

    public StudentChecking findById (String id) {
        return studentCheckingRepository.findById(UUID.fromString(id)).orElseThrow();
    }

    public StudentChecking add(StudentChecking account){
        return studentCheckingRepository.save(account);
    }
}
