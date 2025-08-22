package in.main.hospitalManagement.repository;

import in.main.hospitalManagement.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance , Long> {
}
