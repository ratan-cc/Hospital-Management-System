package in.main.hospitalManagement.controller;

import in.main.hospitalManagement.dto.LoginRequestDto;
import in.main.hospitalManagement.dto.LoginResponseDto;
import in.main.hospitalManagement.dto.SignUpResponseDto;
import in.main.hospitalManagement.entity.Users;
import in.main.hospitalManagement.entity.type.Roles;
import in.main.hospitalManagement.service.UsersService;
import in.main.hospitalManagement.service.serviceimpl.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UsersController(UsersService usersService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // Show a registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Users());
        return "users/register"; // Thymeleaf template: register.html
    }

    // Handle registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Users user, Model model) {
        Optional<Users> existingUser = usersService.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Username already exists!");
            return "users/register";
        }

        usersService.saveUser(user);
        model.addAttribute("success", "User registered successfully!");
        return "users/login"; // Redirect to login page
    }

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody LoginRequestDto signUpRequestDto) {
        Optional<Users> existingUser = usersService.findByUsername(signUpRequestDto.getUsername());
        SignUpResponseDto response = new SignUpResponseDto();

        if (existingUser.isPresent()) {
            response.setMessage("Username already exists!");
            return ResponseEntity.badRequest().body(response);
        }

        Users newUser = new Users();
        newUser.setUsername(signUpRequestDto.getUsername());
        newUser.setPassword(signUpRequestDto.getPassword()); // encode in real apps
        newUser.setRole(signUpRequestDto.getRole()); // ✅ user-chosen role

        usersService.saveUser(newUser);

        response.setId(newUser.getId());
        response.setUsername(newUser.getUsername());
        response.setRole(newUser.getRole());
        response.setMessage("User registered successfully!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        Users user = (Users) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        // Handle a single-role case
        List<Roles> roles = List.of(user.getRole());

        LoginResponseDto response = new LoginResponseDto(
                user.getUsername(),
                token,
                user.getId(),
                roles
        );

        return ResponseEntity.ok(response);
    }





    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login"; // Thymeleaf template: login.html
    }

    // List all users
    @GetMapping("/all")
    public String listUsers(Model model) {
        model.addAttribute("users", usersService.getAllUsers());
        return "users/usersList"; // Thymeleaf template: user_list.html
    }

}
