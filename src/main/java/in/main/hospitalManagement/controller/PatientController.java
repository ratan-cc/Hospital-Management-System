package in.main.hospitalManagement.controller;

import in.main.hospitalManagement.entity.Appointment;
import in.main.hospitalManagement.entity.Patient;
import in.main.hospitalManagement.entity.type.BloodGroupType;
import in.main.hospitalManagement.exception.ResourceNotFoundException;
import in.main.hospitalManagement.service.AppointmentService;
import in.main.hospitalManagement.service.DoctorService;
import in.main.hospitalManagement.service.EmailService;
import in.main.hospitalManagement.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final EmailService emailService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public PatientController(PatientService patientService, EmailService emailService, AppointmentService appointmentService, DoctorService doctorService) {
        this.patientService = patientService;
        this.emailService = emailService;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
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

    @GetMapping("/{id}")
    public String getPatientDetails(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(id);

        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointments);

        return "patients/patientDetails";  // thymeleaf file name
    }
    @GetMapping("/{id}/book")
    public String showBookingForm(@PathVariable Long id,
                                  @RequestParam(required = false) Long doctorId,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  Model model) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        model.addAttribute("patient", patient);
        model.addAttribute("doctors", doctorService.getAllDoctors()); // list of all doctors for dropdown

        List<LocalDateTime> freeSlots = List.of();
        if (doctorId != null && date != null) {
            freeSlots = appointmentService.getFreeSlots(doctorId, date);
            model.addAttribute("selectedDoctorId", doctorId);
            model.addAttribute("selectedDate", date);
        }
        model.addAttribute("freeSlots", freeSlots);

        return "appointments/appointmentBooking";
    }

    @PostMapping("/{id}/book")
    public String bookAppointment(@PathVariable Long id,
                                  @RequestParam Long doctorId,
                                  @RequestParam String appointmentTime,
                                  @RequestParam String reason) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        LocalDateTime apptTime = LocalDateTime.parse(appointmentTime);

        var doctor = doctorService.getDoctorById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appt = new Appointment();
        appt.setPatient(patient);
        appt.setDoctor(doctor);
        appt.setAppointmentTime(apptTime);
        appt.setReason(reason);

        appointmentService.saveAppointment(appt); // already sends mail if implemented

        return "redirect:/patients/" + id;
    }

}