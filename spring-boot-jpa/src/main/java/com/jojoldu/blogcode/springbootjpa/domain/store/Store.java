package com.jojoldu.blogcode.springbootjpa.domain.store;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@NoArgsConstructor
@Getter
@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<Employee> employees = new ArrayList<>();

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.updateStore(this);
    }

    public void addEmployee(Employee employee){
        this.employees.add(employee);
        employee.updateStore(this);
    }
}
