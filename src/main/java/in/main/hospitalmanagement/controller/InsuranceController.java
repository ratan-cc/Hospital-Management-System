package in.main.hospitalmanagement.controller;

import in.main.hospitalmanagement.entity.Insurance;
import in.main.hospitalmanagement.exception.ResourceNotFoundException;
import in.main.hospitalmanagement.service.InsuranceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @GetMapping("/list")
    public String getAllInsurances(Model model) {
        model.addAttribute("insurances", insuranceService.getAllInsurances());
        return "insurances/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("insurance", new Insurance());
        return "insurances/form";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Insurance insurance = insuranceService.getInsuranceById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found with ID: " + id));
        model.addAttribute("insurance", insurance);
        return "insurances/form";
    }

    @PostMapping("/save")
    public String saveInsurance(@ModelAttribute("insurance") Insurance insurance) {
        insuranceService.saveInsurance(insurance);
        return "redirect:/insurances/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteInsurance(@PathVariable Long id) {
        if (!insuranceService.deleteInsurance(id)) {
            throw new ResourceNotFoundException("Insurance not found with ID: " + id);
        }
        return "redirect:/insurances/list";
    }
}