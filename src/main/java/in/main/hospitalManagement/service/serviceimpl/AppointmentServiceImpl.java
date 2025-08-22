package in.main.hospitalManagement.service.serviceimpl;

import in.main.hospitalManagement.entity.Appointment;
import in.main.hospitalManagement.repository.AppointmentRepository;
import in.main.hospitalManagement.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        return appointmentRepository.findById(id).map(existing -> {
            existing.setAppointmentTime(updatedAppointment.getAppointmentTime());
            existing.setReason(updatedAppointment.getReason());
            existing.setDoctor(updatedAppointment.getDoctor());
            existing.setPatient(updatedAppointment.getPatient());
            return appointmentRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Override
    public boolean deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
