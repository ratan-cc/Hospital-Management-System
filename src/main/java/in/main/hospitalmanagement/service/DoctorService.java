package in.main.hospitalmanagement.service;

import in.main.hospitalmanagement.entity.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor saveDoctor(Doctor doctor);
    Doctor updateDoctor(Long id, Doctor doctor);
    boolean deleteDoctor(Long id);
    Optional<Doctor> getDoctorById(Long id);
    List<Doctor> getAllDoctors();




}
