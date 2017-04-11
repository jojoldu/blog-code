package com.blogcode.order;

import com.blogcode.product.Product;
import com.blogcode.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Service
@Transactional
public class PurchaseOrderService {

    private PurchaseOrderRepository purchaseOrderRepository;
    private ProductRepository productRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, ProductRepository productRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productRepository = productRepository;
    }

    public Long order(Long buyerId, List<Long> productIds) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.save(PurchaseOrder.createOrder(buyerId));

        for(Long productId : productIds){
            Product product = findProductById(productId);
            purchaseOrder.addProduct(product);
        }

        return purchaseOrder.getId();
    }

    private Product findProductById(Long productId){
        return Optional.ofNullable(productRepository.findOne(productId))
                .orElseThrow(() -> new RuntimeException("해당 제품 ID는 없는 ID입니다."));
    }

}
