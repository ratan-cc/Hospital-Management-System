package in.main.hospitalmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false , length = 100)
    private String name;

    @OneToOne
    @JoinColumn(name = "head_doctor_id", referencedColumnName = "id")
    private Doctor headDoctor;

    @ManyToMany(mappedBy = "departments")
    private Set<Doctor> doctors =new HashSet<>();
}
