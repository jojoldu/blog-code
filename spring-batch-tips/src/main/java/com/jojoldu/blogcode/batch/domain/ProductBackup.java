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
public class ProductBackup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long originId;

    private String name;
    private long price;
    private int categoryNo;
    private LocalDate createDate;

    @Builder
    public ProductBackup(Product product) {
        this.originId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.categoryNo = product.getCategoryNo();
        this.createDate = product.getCreateDate();
    }
}
