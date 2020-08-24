package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import model.jpa.spring.ContractEmployee;

public interface ContractEmployeeRepository extends JpaRepository<ContractEmployee, Long> {


}
