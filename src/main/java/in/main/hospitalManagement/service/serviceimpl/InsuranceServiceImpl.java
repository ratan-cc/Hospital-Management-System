package in.main.hospitalManagement.service.serviceimpl;

import in.main.hospitalManagement.entity.Insurance;
import in.main.hospitalManagement.repository.InsuranceRepository;
import in.main.hospitalManagement.service.InsuranceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepository;

    public InsuranceServiceImpl(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    @Override
    public Insurance saveInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    @Override
    public Insurance updateInsurance(Long id, Insurance updatedInsurance) {
        return insuranceRepository.findById(id).map(existing -> {
            existing.setPolicyNumber(updatedInsurance.getPolicyNumber());
            existing.setProvider(updatedInsurance.getProvider());
            existing.setValidUntil(updatedInsurance.getValidUntil());
            return insuranceRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Insurance not found"));
    }

    @Override
    public boolean deleteInsurance(Long id) {
        if (insuranceRepository.existsById(id)) {
            insuranceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Insurance> getInsuranceById(Long id) {
        return insuranceRepository.findById(id);
    }

    @Override
    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }
}
