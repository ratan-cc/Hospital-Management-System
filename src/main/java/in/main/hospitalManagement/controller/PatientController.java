package in.main.hospitalManagement.controller;

import in.main.hospitalManagement.entity.Patient;
import in.main.hospitalManagement.entity.type.BloodGroupType;
import in.main.hospitalManagement.exception.ResourceNotFoundException;
import in.main.hospitalManagement.service.EmailService;
import in.main.hospitalManagement.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    private EmailService emailService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/list")
    public String getAllPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "patients/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("bloodGroups", BloodGroupType.values());
        return "patients/form";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
        model.addAttribute("patient", patient);
        model.addAttribute("bloodGroups", BloodGroupType.values());
        return "patients/form";
    }


    @PostMapping("/save")
    public String savePatient(@ModelAttribute("patient") Patient patient) {
        patientService.savePatient(patient);

        // 2. Send confirmation email
        String subject = "Welcome to Our Hospital System";
        String body = "Dear " + patient.getName() + ",\n\n" +
                "Your registration was successful.\n" +
                "We have saved your details in our system.\n\n" +
                "Regards,\nHospital Team";

        emailService.sendEmail(patient.getEmail(), subject, body);

        return "redirect:/patients/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        if (!patientService.deletePatient(id)) {
            throw new ResourceNotFoundException("Patient not found with ID : " + id);
        }
        return "redirect:/patients/list";
    }

    @PostMapping("/deleteSelected")
    public String deleteSelectedPatients(@RequestParam("patientIds") List<Long> patientIds) {
        for (Long id : patientIds) {
            patientService.deletePatient(id);
        }
        return "redirect:/patients/list";
    }


    @GetMapping("/search")
    public String searchByMobile(@RequestParam String mobile, Model model) {
        List<Patient> patients = patientService.getPatientsByMobile(mobile);

        if (patients.isEmpty()) {
            model.addAttribute("message", "No patients found with mobile: " + mobile);
        } else {
            model.addAttribute("patients", patients);
            model.addAttribute("patientCount", patients.size());

            // Collect all doctors from appointments
            List<String> doctors = patients.stream()
                    .flatMap(p -> p.getAppointment().stream())
                    .map(a -> a.getDoctor().getName())
                    .distinct()
                    .toList();

            model.addAttribute("doctors", doctors);
        }
        return "patients/searchResult";
    }

}