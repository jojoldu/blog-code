package com.blogcode.order;

import com.blogcode.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 11.
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
    private Long buyerId;

    @OneToMany
    private List<Product> products = new ArrayList<>();

    @Column
    private Status status;

    public void cancelOrder(){
        status = Status.CANCEL;
    }

    @Builder
    private PurchaseOrder(Long buyerId, Status status) {
        this.buyerId = buyerId;
        this.status = status;
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public static PurchaseOrder createOrder(Long buyerId){
        return PurchaseOrder.builder()
                .buyerId(buyerId)
                .status(Status.ORDER_COMPLETE)
                .build();
    }

    private enum Status {
        ORDER_COMPLETE("주문완료"),
        SHIPPING("배송중"),
        DELIVERY_COMPLETED("배송완료"),
        CANCEL("취소");

        private String viewName;

        Status(String viewName) {
            this.viewName = viewName;
        }

        public String getViewName() {
            return viewName;
        }
    }
}
