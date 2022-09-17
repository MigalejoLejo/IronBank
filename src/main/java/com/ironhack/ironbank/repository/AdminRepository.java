package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepository extends JpaRepository<Admin,String> {

    Admin findByUsername(String username);
}