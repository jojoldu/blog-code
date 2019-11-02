package com.jojoldu.blogcode.springbootjpa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@NoArgsConstructor
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private long price;

    @ManyToOne
    @JoinColumn(name = "store_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_STORE"))
    private Store store;

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public void updateStore(Store store){
        this.store = store;
    }
}
