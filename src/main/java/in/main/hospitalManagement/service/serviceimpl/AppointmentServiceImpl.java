package in.main.hospitalManagement.service.serviceimpl;

import in.main.hospitalManagement.entity.Appointment;
import in.main.hospitalManagement.repository.AppointmentRepository;
import in.main.hospitalManagement.service.AppointmentService;
import in.main.hospitalManagement.service.EmailService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.emailService = emailService;
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        Appointment saved = appointmentRepository.save(appointment);
        // Email to Patient
        if (saved.getPatient() != null && saved.getPatient().getEmail() != null) {
            emailService.sendEmail(
                    saved.getPatient().getEmail(),
                    "Appointment Confirmation",
                    "Dear " + saved.getPatient().getName() + ",\n\n" +
                            "Your appointment with Dr. " + saved.getDoctor().getName() +
                            " is scheduled for " + saved.getAppointmentTime() + ".\nReason: " + saved.getReason() +
                            "\n\nRegards,\nHospital Team"
            );
        }

        // Email to Doctor
        if (saved.getDoctor() != null && saved.getDoctor().getEmail() != null) {
            emailService.sendEmail(
                    saved.getDoctor().getEmail(),
                    "New Appointment Scheduled",
                    "Dear Dr. " + saved.getDoctor().getName() + ",\n\n" +
                            "You have a new appointment with patient " + saved.getPatient().getName() +
                            " on " + saved.getAppointmentTime() +
                            ".\nReason: " + saved.getReason() +
                            "\n\nRegards,\nHospital Team"
            );
        }

        return saved;
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

    @Override
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public boolean isDoctorAvailable(Long doctorId, LocalDateTime time) {
        return appointmentRepository.findByDoctorIdAndAppointmentTime(doctorId, time).isEmpty();
    }

    @Override
    public List<LocalDateTime> getFreeSlots(Long doctorId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Appointment> bookedAppointments =
                appointmentRepository.findByDoctorIdAndDate(doctorId, startOfDay, endOfDay);

        List<LocalDateTime> bookedTimes = bookedAppointments.stream()
                .map(Appointment::getAppointmentTime)
                .toList();

        List<LocalDateTime> freeSlots = new ArrayList<>();
        LocalDateTime slot = date.atTime(9, 0); // 9 AM start
        LocalDateTime endTime = date.atTime(17, 0); // 5 PM end

        while (!slot.isAfter(endTime)) {
            if (!bookedTimes.contains(slot)) {
                freeSlots.add(slot);
            }
            slot = slot.plusMinutes(30);
        }

        return freeSlots;
    }


}
