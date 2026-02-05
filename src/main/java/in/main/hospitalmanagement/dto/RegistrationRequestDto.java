package in.main.hospitalmanagement.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {
    private String username;
    private String password;
    private String role;
}