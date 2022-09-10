package com.ironhack.ironbank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@RestController
public class HelloController {

    @GetMapping("/public/hello")
    public ResponseEntity<String> helloPublic() {
        return ResponseEntity.ok("Hello public user");
    }

    @GetMapping("/accountholder/hello")
    public ResponseEntity<String> helloMember(Principal principal) {
        return ResponseEntity.ok("Hello dear member" + principal.getName());
    }


    @GetMapping("/admin/hello")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("Have a very nice day, admin");
    }

    @RolesAllowed("admin")
    @GetMapping("/public/hello-fake-public")
    public ResponseEntity<String> helloCustom() {
        return ResponseEntity.ok("Nice day, it appears to be public but not");
    }


}
