package model.jpa.spring;

import javax.persistence.*;

@Entity
@Table(name = "regular_employee")
public class RegularEmployee extends Employee{

    private Integer salary;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
