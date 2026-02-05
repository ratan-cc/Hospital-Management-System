package in.main.hospitalmanagement.service;

import in.main.hospitalmanagement.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment saveAppointment(Appointment appointment);

    Appointment updateAppointment(Long id, Appointment appointment);

    boolean deleteAppointment(Long id);

    boolean cancelAppointment(Long id);

    Optional<Appointment> getAppointmentById(Long id);

    List<Appointment> getAllAppointments();

    List<Appointment> getAppointmentsByPatient(Long patientId);

    List<Appointment> getAppointmentsByDoctor(Long doctorId);

    boolean isDoctorAvailable(Long doctorId, java.time.LocalDateTime time);

    List<LocalDateTime> getFreeSlots(Long doctorId, LocalDate date);
}
