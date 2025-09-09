package in.main.hospitalManagement.dto;

import in.main.hospitalManagement.entity.type.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDto {
    private Long id;
    private String username;
    private Roles role;
    private String message;
}
