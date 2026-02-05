package in.main.hospitalmanagement.service;

import in.main.hospitalmanagement.entity.Insurance;

import java.util.List;
import java.util.Optional;

public interface InsuranceService {
    Insurance saveInsurance(Insurance insurance);
    Insurance updateInsurance(Long id, Insurance insurance);
    boolean deleteInsurance(Long id);
    Optional<Insurance> getInsuranceById(Long id);
    List<Insurance> getAllInsurances();
}
