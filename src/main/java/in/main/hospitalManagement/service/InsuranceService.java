package in.main.hospitalManagement.service;

import in.main.hospitalManagement.entity.Insurance;

import java.util.List;
import java.util.Optional;

public interface InsuranceService {
    Insurance saveInsurance(Insurance insurance);
    Insurance updateInsurance(Long id, Insurance insurance);
    boolean deleteInsurance(Long id);
    Optional<Insurance> getInsuranceById(Long id);
    List<Insurance> getAllInsurances();
}
