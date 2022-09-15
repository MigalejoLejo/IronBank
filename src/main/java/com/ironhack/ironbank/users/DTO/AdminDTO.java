package com.ironhack.ironbank.users.DTO;

import com.ironhack.ironbank.users.model.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    String id;
    String username;
    String password;
    String email;

    public static AdminDTO fromEntity (Admin admin){
        return new AdminDTO(
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getEmail()
        );
    }

}

