package in.main.hospitalManagement.service.serviceimpl;

import in.main.hospitalManagement.entity.Doctor;
import in.main.hospitalManagement.repository.DoctorRepository;
import in.main.hospitalManagement.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        return doctorRepository.findById(id).map(existing -> {
            existing.setName(updatedDoctor.getName());
            existing.setSpecialization(updatedDoctor.getSpecialization());
            existing.setEmail(updatedDoctor.getEmail());
            existing.getDepartments().clear();
            existing.getDepartments().addAll(updatedDoctor.getDepartments());
            return doctorRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    @Override
    public boolean deleteDoctor(Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
