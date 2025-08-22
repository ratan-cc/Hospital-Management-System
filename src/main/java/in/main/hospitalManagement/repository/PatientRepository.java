package in.main.hospitalManagement.repository;

import in.main.hospitalManagement.entity.Patient;
import in.main.hospitalManagement.entity.type.BloodGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByMobile(String mobile);

//    List<Patient> findByName(String name);
//    List<Patient> findByBirthDateOrEmail(LocalDate birthDate , String email);
//    List<Patient> findByBirthDateBetween(LocalDate startDate , LocalDate endDate);
//    List<Patient> findNameByContainingOrderByIdDesc (String query);
//
//    //JPQL ( Jakarta persistence query language )
//    @Query("SELECT p FROM Patient p WHERE P.bloodGroup = ?1")
//    List<Patient> findByBloodGroup(@Param("bloodGroup") BloodGroupType bloodGroup);
//
//    // we use this to save query from SqlAttack
//    @Query("SELECT p FROM Patient p WHERE P.birthDate > :birthdate ")
//    List<Patient> findByBornAfterDate(@Param("birthdate") LocalDate birthDate);
}
