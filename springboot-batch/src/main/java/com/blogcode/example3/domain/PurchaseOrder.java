package com.blogcode.example3.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 12.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String memo;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> productList;

    @Builder
    public PurchaseOrder(String memo, List<Product> productList) {
        this.memo = memo;
        this.productList = productList;
    }
}
