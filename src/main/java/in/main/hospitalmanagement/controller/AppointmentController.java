package in.main.hospitalmanagement.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import in.main.hospitalmanagement.entity.Appointment;
import in.main.hospitalmanagement.entity.Doctor;
import in.main.hospitalmanagement.exception.ResourceNotFoundException;
import in.main.hospitalmanagement.service.AppointmentService;
import in.main.hospitalmanagement.service.DoctorService;
import in.main.hospitalmanagement.service.EmailService;
import in.main.hospitalmanagement.service.PatientService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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

    @PostMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id, @RequestParam Long patientId) {
        if (!appointmentService.cancelAppointment(id)) {
            throw new ResourceNotFoundException("Appointment not found with ID: " + id);
        }
        return "redirect:/patients/" + patientId;
    }
    @GetMapping("/downloadSlip/{id}")
    public ResponseEntity<InputStreamResource> downloadSlip(@PathVariable Long id) throws Exception {
        Appointment appointment = appointmentService.getAppointmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        // Create PDF document
        ByteArrayInputStream bis = generateAppointmentSlip(appointment);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=appointment-slip.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    private ByteArrayInputStream generateAppointmentSlip(Appointment appointment) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Hospital Management System"));
            document.add(new Paragraph("Appointment Slip"));
            document.add(new Paragraph("----------------------------------"));
            document.add(new Paragraph("Patient: " + appointment.getPatient().getName()));
            document.add(new Paragraph("Doctor: " + appointment.getDoctor().getName()));
            document.add(new Paragraph("Date & Time: " + appointment.getAppointmentTime()));
            document.add(new Paragraph("Reason: " + appointment.getReason()));
            document.close();
        } catch (DocumentException e) {
            // Handle exception
        }
        return new ByteArrayInputStream(out.toByteArray());
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