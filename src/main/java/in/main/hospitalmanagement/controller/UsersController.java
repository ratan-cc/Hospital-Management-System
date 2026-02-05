package in.main.hospitalmanagement.controller;

import in.main.hospitalmanagement.dto.LoginRequestDto;
import in.main.hospitalmanagement.dto.LoginResponseDto;
import in.main.hospitalmanagement.dto.RegistrationRequestDto;
import in.main.hospitalmanagement.entity.Users;
import in.main.hospitalmanagement.entity.type.Roles;
import in.main.hospitalmanagement.service.UsersService;
import in.main.hospitalmanagement.service.serviceimpl.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public UsersController(UsersService usersService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService; }


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // SUCCESS → Spring Security handles session + redirect
        return "redirect:/patients";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequestDto());
        model.addAttribute("roles", Roles.values());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequestDto dto, Model model) {
        if (usersService.findByUsername(dto.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists!");
            model.addAttribute("roles", Roles.values());
            return "register";
        }

        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // encoded in service
        user.setRole(dto.getRole() == null ? Roles.ROLE_DEFAULT : Roles.valueOf(dto.getRole()));

        usersService.saveUser(user);
        return "redirect:/users/login";
    }
}
