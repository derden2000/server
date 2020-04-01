package pro.antonshu.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.antonshu.server.entities.User;
import pro.antonshu.server.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/hello")
    public String hello(Principal principal) {
        UserDetails currentUser
                = (UserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findOneByLogin(currentUser.getUsername());
        System.out.println("Login: " + user.getLogin());
        System.out.println("Authorities: " + currentUser.getAuthorities());
        return String.format("Hello %s! Please Sign in to continue.", user.getLogin());
    }
}
