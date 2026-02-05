package in.main.hospitalmanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Collection;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home(Authentication authentication) {
        // If a user is NOT authenticated, redirect to login page
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login";
        }

        // Check user roles and redirect to a role-specific dashboard
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            return "redirect:/doctors/dashboard";
        }
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_NURSE"))) {
            return "redirect:/nurses/dashboard";
        }
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_RECEPTIONIST"))) {
            return "redirect:/receptionists/dashboard";
        }
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PHARMACIST"))) {
            return "redirect:/pharmacists/dashboard";
        }
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ACCOUNTANT"))) {
            return "redirect:/accountants/dashboard";
        }
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_DEFAULT"))) {
            return "redirect:/patients/dashboard";
        }

        // Fallback for any other authenticated user or if no specific mapping is found
        return "index";
    }
}