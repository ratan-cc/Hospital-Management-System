package in.main.hospitalmanagement.repository;

import in.main.hospitalmanagement.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance , Long> {
}
