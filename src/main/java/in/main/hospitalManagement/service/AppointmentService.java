package in.main.hospitalManagement.service;

import in.main.hospitalManagement.entity.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment saveAppointment(Appointment appointment);
    Appointment updateAppointment(Long id, Appointment appointment);
    boolean deleteAppointment(Long id);
    Optional<Appointment> getAppointmentById(Long id);
    List<Appointment> getAllAppointments();
}
