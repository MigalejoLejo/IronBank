package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, UUID> {


}
