package in.main.hospitalManagement.entity;

import in.main.hospitalManagement.entity.type.BloodGroupType;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(of = "id")

@Table(
        uniqueConstraints = {
//                @UniqueConstraint(name = "Unique email id", columnNames = {"email"}),
                @UniqueConstraint(name = "" , columnNames = {"name" , "birthDate"})
        },
        indexes = {
             @Index(name = "idx_patient_birth_date", columnList = "birthDate")
        }
)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 15) // Mobile must be unique
    private String mobile;

    private String gender;

    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = {CascadeType.ALL} , orphanRemoval = true)
    @JoinColumn //owning side
    private Insurance insurance;


    @OneToMany(mappedBy = "patient" , cascade = {CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Appointment> appointment = new ArrayList<>();

}
