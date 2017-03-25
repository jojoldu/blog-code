package com.blogcode.dto;

import com.blogcode.domain.Sales;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 23.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class SalesConverter {

    private SalesConverter() {}

    public static Sales createSales(List<PaymentDto> payments){
        Sales sales = Sales.builder()
                .ownerId(payments.get(0).getOwnerId())
                .calculateCode(payments.get(0).getCalculateCode())
                .payDate(payments.get(0).getPayDate())
                .build();

        payments.forEach(paymentDto -> sales.add(paymentDto.getPrice(), paymentDto.getPaymentMethod()));
        return sales;
    }

    public static List<Sales> createSalesList(Stream<List<PaymentDto>> paymentDtoStream){
        return paymentDtoStream
                .map(SalesConverter::createSales)
                .collect(Collectors.toList());
    }
}
