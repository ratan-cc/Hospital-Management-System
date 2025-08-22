package in.main.hospitalManagement.service;

import in.main.hospitalManagement.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient savePatient(Patient patient);
    Patient updatePatient(Long id, Patient patient);
    List<Patient> getPatientsByMobile(String mobile);
    boolean deletePatient(Long id);
    Optional<Patient> getPatientById(Long id);
    List<Patient> getAllPatients();
}
