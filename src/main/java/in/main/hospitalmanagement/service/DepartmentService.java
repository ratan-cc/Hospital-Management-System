package in.main.hospitalmanagement.service;

import in.main.hospitalmanagement.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department saveDepartment(Department department);
    Department updateDepartment(Long id, Department department);
    boolean deleteDepartment(Long id);
    Optional<Department> getDepartmentById(Long id);
    List<Department> getAllDepartments();
}
