package in.main.hospitalManagement.dto;

import in.main.hospitalManagement.entity.type.Roles;
import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
    private Roles role;
}
