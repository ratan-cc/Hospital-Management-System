package in.main.hospitalmanagement.controller;

import in.main.hospitalmanagement.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsersService usersService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final InsuranceService insuranceService;
    private final DepartmentService departmentService;

    public AdminController(UsersService usersService, DoctorService doctorService, PatientService patientService, AppointmentService appointmentService, InsuranceService insuranceService, DepartmentService departmentService) {
        this.usersService = usersService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.insuranceService = insuranceService;
        this.departmentService = departmentService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        // Fetch data for the dashboard summary
        long totalUsers = usersService.getAllUsers().size();
        long totalDoctors = doctorService.getAllDoctors().size();
        long totalPatients = patientService.getAllPatients().size();
        long totalAppointments = appointmentService.getAllAppointments().size();
        long totalInsurances = insuranceService.getAllInsurances().size();
        long totalDepartments = departmentService.getAllDepartments().size();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalDoctors", totalDoctors);
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("totalAppointments", totalAppointments);
        model.addAttribute("totalInsurances", totalInsurances);
        model.addAttribute("totalDepartments", totalDepartments);

        return "admin/dashboard";
    }
}