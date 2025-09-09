package in.main.hospitalManagement.entity;

import in.main.hospitalManagement.entity.type.Roles;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Users{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JoinColumn(unique = true, nullable = false)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;
}
