package pro.antonshu.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/hello")
    public String hello(Principal principal) {
        UserDetails currentUser
                = (UserDetails) ((Authentication) principal).getPrincipal();
        return "Hello " + currentUser.getUsername()+"!";
    }
}
