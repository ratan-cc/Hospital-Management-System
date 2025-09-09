package in.main.hospitalManagement.dto;

import in.main.hospitalManagement.entity.type.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String username;
    private String jwt;
    private Long userId;
    private List<Roles> roles;
}
