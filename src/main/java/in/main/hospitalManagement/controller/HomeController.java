package in.main.hospitalManagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        // If a user is NOT authenticated, redirect to login page
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login";
        }

        // If authenticated, show index page
        return "index";
    }
}
