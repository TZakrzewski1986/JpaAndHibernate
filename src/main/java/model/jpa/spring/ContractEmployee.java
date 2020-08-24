package model.jpa.spring;

import javax.persistence.*;

@Entity
@Table(name = "contract_employee")
public class ContractEmployee extends Employee {

    @Column(name = "pay_per_hour")
    private Integer payPerHour;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Integer getPayPerHour() {
        return payPerHour;
    }

    public void setPayPerHour(Integer payPerHour) {
        this.payPerHour = payPerHour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
