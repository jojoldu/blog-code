package com.blogcode.example3.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 12.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long purchaseOrderId;

    @Column
    private String productIdList;

    @Builder
    public History(Long purchaseOrderId, List<Product> productIdList) {
        this.purchaseOrderId = purchaseOrderId;
        this.productIdList = productIdList.stream()
                .map(product -> String.valueOf(product.getId()))
                .collect(Collectors.joining(", "));
    }
}
