package com.jojoldu.springmockspybean.dto;

import com.jojoldu.springmockspybean.domain.product.Product;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 22.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
public class OrderResponseDto {

    private String customerName;
    private List<ProductDto> products;

    public OrderResponseDto(String customerName, List<Product> products) {
        this.customerName = customerName;
        this.products = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class ProductDto {

        private String name;
        private long price;

        public ProductDto(Product product) {
            this.name = product.getName();
            this.price = product.getPrice();
        }
    }

}
