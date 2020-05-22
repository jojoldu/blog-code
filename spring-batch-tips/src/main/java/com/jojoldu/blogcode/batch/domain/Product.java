package com.jojoldu.blogcode.batch.domain;

/**
 * Created by jojoldu@gmail.com on 20/08/2018
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private long price;
    private int categoryNo;
    private LocalDate createDate;

    @Builder
    public Product(String name, long price, int categoryNo, LocalDate createDate) {
        this.name = name;
        this.price = price;
        this.categoryNo = categoryNo;
        this.createDate = createDate;
    }
}
