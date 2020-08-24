package service;

import model.jpa.Student;
import org.springframework.stereotype.Service;
import repository.ContractEmployeeRepository;
import repository.RegularEmployeeRepository;
import repository.StudentRepository;
import model.jpa.spring.ContractEmployee;
import model.jpa.spring.RegularEmployee;

import java.util.List;
import java.util.Optional;

@Service
public class SpringService {
    private StudentRepository studentRepository;

    private ContractEmployeeRepository contractEmployeeRepository;

    private RegularEmployeeRepository regularEmployeeRepository;

    public SpringService(StudentRepository studentRepository, ContractEmployeeRepository contractEmployeeRepository, RegularEmployeeRepository regularEmployeeRepository) {
        this.studentRepository = studentRepository;
        this.contractEmployeeRepository = contractEmployeeRepository;
        this.regularEmployeeRepository = regularEmployeeRepository;
    }

    public void getAllStudents() {
        List<Student> students = studentRepository.findAll();

        System.out.println(students);
    }

    public void findStudentByName(String name) {
        Optional<Student> studentByName = studentRepository.findStudentByName(name);
        System.out.println(studentByName.get());
    }

    public void saveEmployees() {
        ContractEmployee contractEmployee = new ContractEmployee();
        contractEmployee.setPayPerHour(10);
        contractEmployee.setName("Andrzej");

        RegularEmployee regularEmployee = new RegularEmployee();
        regularEmployee.setSalary(2500);
        regularEmployee.setName("Lech");

        contractEmployeeRepository.save(contractEmployee);
        regularEmployeeRepository.save(regularEmployee);
    }
}
