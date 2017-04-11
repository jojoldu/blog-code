package com.blogcode.order;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>{
}
