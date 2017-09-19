package com.jojoldu.springmockspybean.domain.order;

import com.jojoldu.springmockspybean.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 19.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */


@Entity
@Getter
@NoArgsConstructor
public class OrderProductMap {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_order_id", foreignKey = @ForeignKey(name = "FK_CUSTOMER_ORDER_MAP"))
    private CustomerOrder customerOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_MAP"))
    private Product product;

    public OrderProductMap(CustomerOrder customerOrder, Product product) {
        this.customerOrder = customerOrder;
        this.product = product;
    }
}
