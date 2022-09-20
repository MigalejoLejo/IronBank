package com.ironhack.ironbank.model;

import com.ironhack.ironbank.DTO.AdminDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin  {

    @Id
    String id;
    String username;
    String email;
    String password;



    public Admin(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;

    }

    public static Admin fromDTO(AdminDTO adminDTO){
        return new Admin(
                adminDTO.getId(),
                adminDTO.getUsername(),
                adminDTO.getEmail(),
                adminDTO.getPassword()
        );
    }

}
