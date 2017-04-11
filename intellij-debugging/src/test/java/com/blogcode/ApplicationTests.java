package com.blogcode;

import com.blogcode.order.PurchaseOrderRepository;
import com.blogcode.order.PurchaseOrderService;
import com.blogcode.product.Product;
import com.blogcode.product.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private ProductRepository productRepository;


	@Test
	public void 주문하기() {
		//given
		Long buyerId = 10L;
		Long productId = productRepository.save(Product.builder()
				.amount(10000L)
				.name("손수건")
				.build())
				.getId();

		//then
		Long ownerId = purchaseOrderService.order(buyerId, Arrays.asList(productId));

		assertThat(ownerId, is(1L));
	}

}
