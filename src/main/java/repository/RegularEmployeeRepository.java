package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import model.jpa.spring.RegularEmployee;

public interface RegularEmployeeRepository extends JpaRepository<RegularEmployee, Long> {

}
