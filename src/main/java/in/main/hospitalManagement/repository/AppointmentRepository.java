package in.main.hospitalManagement.repository;

import in.main.hospitalManagement.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByDoctorIdAndAppointmentTime(Long doctorId, LocalDateTime time);

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndDate(@Param("doctorId") Long doctorId,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);


}
