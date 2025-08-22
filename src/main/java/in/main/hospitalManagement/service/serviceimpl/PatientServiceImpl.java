package in.main.hospitalManagement.service.serviceimpl;

import in.main.hospitalManagement.entity.Patient;
import in.main.hospitalManagement.repository.PatientRepository;
import in.main.hospitalManagement.service.EmailService;
import in.main.hospitalManagement.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final EmailService emailService;

    public PatientServiceImpl(PatientRepository patientRepository, EmailService emailService) {
        this.patientRepository = patientRepository;
        this.emailService = emailService;
    }


    @Override
    public Patient savePatient(Patient patient) {
        Patient saved = patientRepository.save(patient);

        // send a simple welcome email (text)
        if (saved.getEmail() != null && !saved.getEmail().isBlank()) {
            emailService.sendText(
                    saved.getEmail(),
                    "Welcome to Our Hospital",
                    "Dear " + saved.getName() + ",\n\nYour registration is successful.\n\nRegards,\nHospital Team"
            );
        }
        // send email after save
        String subject = "Welcome, " + saved.getName();
        String body = "Hello " + saved.getName() + ",\n\n"
                + "Your registration is successful. "
                + "Your registered mobile is: " + saved.getMobile();

        emailService.sendEmail(saved.getEmail(), subject, body);
        return saved;
    }


    @Override
    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedPatient.getName());
                    existing.setBirthDate(updatedPatient.getBirthDate());
                    existing.setEmail(updatedPatient.getEmail());
                    existing.setMobile(updatedPatient.getMobile());
                    existing.setGender(updatedPatient.getGender());
                    existing.setBloodGroup(updatedPatient.getBloodGroup());
                    existing.setInsurance(updatedPatient.getInsurance());
                    existing.setAppointment(updatedPatient.getAppointment());
                    Patient saved = patientRepository.save(existing);

                    //Notify patient about update
                    if (saved.getEmail() != null && !saved.getEmail().isBlank()) {
                        emailService.sendText(
                                saved.getEmail(),
                                "Your Profile Was Updated",
                                "Hello " + saved.getName() + ",\n\nYour profile has been updated.\n\nRegards,\nHospital Team"
                        );
                    }
                    return saved;
                }).orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
    }

    @Override
    public List<Patient> getPatientsByMobile(String mobile) {
        return patientRepository.findByMobile(mobile);
    }

    @Override
    public boolean deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {

        return patientRepository.findById(id);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
