package in.main.hospitalManagement.service.serviceimpl;

import in.main.hospitalManagement.entity.Department;
import in.main.hospitalManagement.repository.DepartmentRepository;
import in.main.hospitalManagement.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department saveDepartment(Department department) {

        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Long id, Department updatedDepartment) {
        return departmentRepository.findById(id).map(existing -> {
            existing.setName(updatedDepartment.getName());
            existing.setHeadDoctor(updatedDepartment.getHeadDoctor());
            existing.getDoctors().clear();
            existing.getDoctors().addAll(updatedDepartment.getDoctors());

            return departmentRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Override
    public boolean deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
