package in.main.hospitalManagement.entity;

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
    private Doctor headDoctor;

    @ManyToMany
    @JoinTable(
//            name = "dept_Doc",
//            joinColumns = @JoinColumn(name = "Dpt_id"),
//            inverseJoinColumns = @JoinColumn(name = "Doctor_id")
    )
    private Set<Doctor> doctors =new HashSet<>();
}
