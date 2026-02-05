package in.main.hospitalmanagement.dto;

import in.main.hospitalmanagement.entity.type.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDto {

    private String username;
    private Roles role;
    private String message;

    public void setId(Long id) {
    }
}
