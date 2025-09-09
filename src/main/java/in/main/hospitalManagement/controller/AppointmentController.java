package in.main.hospitalManagement.controller;

import in.main.hospitalManagement.entity.Appointment;
import in.main.hospitalManagement.entity.Doctor;
import in.main.hospitalManagement.exception.ResourceNotFoundException;
import in.main.hospitalManagement.service.AppointmentService;
import in.main.hospitalManagement.service.DoctorService;
import in.main.hospitalManagement.service.EmailService;
import in.main.hospitalManagement.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final EmailService emailService;

    public AppointmentController(AppointmentService appointmentService, DoctorService doctorService, PatientService patientService, EmailService emailService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.emailService = emailService;
    }

    @GetMapping("/book/{doctorId}")
    public String showBookingForm(@PathVariable Long doctorId, Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        model.addAttribute("doctor", doctor);
        model.addAttribute("appointment", new Appointment());
        return "appointmentBooking";
    }

    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute Appointment appointment, Model model) {
        if (!appointmentService.isDoctorAvailable(
                appointment.getDoctor().getId(), appointment.getAppointmentTime())) {
            model.addAttribute("error", "Doctor not available at this time, please choose another slot.");
            return "appointmentBooking";
        }

        Appointment saved = appointmentService.saveAppointment(appointment);

        // Send emails to patient and doctor
        emailService.sendEmail(
                saved.getPatient().getEmail(),
                "Your Appointment is Confirmed",
                "Dear " + saved.getPatient().getName() +
                        ", your appointment with Dr. " + saved.getDoctor().getName() +
                        " is confirmed for " + saved.getAppointmentTime());

        emailService.sendEmail(
                saved.getDoctor().getEmail(),
                "New Appointment Scheduled",
                "Dr. " + saved.getDoctor().getName() +
                        ", you have a new appointment with patient " + saved.getPatient().getName() +
                        " at " + saved.getAppointmentTime());

        return "redirect:/patients/" + saved.getPatient().getId(); // back to patient details page
    }


    @GetMapping("/list")
    public String getAllAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointments/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients());
        return "appointments/form";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients());
        return "appointments/form";
    }

    @PostMapping("/save")
    public String saveAppointment(@ModelAttribute("appointment") Appointment appointment) {
        appointmentService.saveAppointment(appointment);
        return "redirect:/appointments/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        if (!appointmentService.deleteAppointment(id)) {
            throw new ResourceNotFoundException("Appointment not found with ID: " + id);
        }
        return "redirect:/appointments/list";
    }
}