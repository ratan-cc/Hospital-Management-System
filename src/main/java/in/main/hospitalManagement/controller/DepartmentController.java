package in.main.hospitalManagement.controller;

import in.main.hospitalManagement.entity.Department;
import in.main.hospitalManagement.exception.ResourceNotFoundException;
import in.main.hospitalManagement.service.DepartmentService;
import in.main.hospitalManagement.service.DoctorService; // Inject DoctorService
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DoctorService doctorService; // Add DoctorService

    public DepartmentController(DepartmentService departmentService, DoctorService doctorService) {
        this.departmentService = departmentService;
        this.doctorService = doctorService;
    }

    @GetMapping("/list")
    public String getAllDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "departments/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("doctors", doctorService.getAllDoctors()); // Provide list of doctors
        return "departments/form";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        model.addAttribute("department", department);
        model.addAttribute("doctors", doctorService.getAllDoctors()); // Provide list of doctors
        return "departments/form";
    }

    @PostMapping("/save")
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentService.saveDepartment(department);
        return "redirect:/departments/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        if (!departmentService.deleteDepartment(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }
        return "redirect:/departments/list";
    }
}