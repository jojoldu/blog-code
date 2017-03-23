package com.blogcode.dto;

import com.blogcode.domain.Sales;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 23.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Getter
public class SalesDto {
    private Long ownerId;
    private String calculateCode;
    private int totalAmount;
    private int mobileAmount;
    private int creditCardAmount;
    private int cashAmount;

    public Sales toEntity(){
        return Sales.builder()
                .ownerId(ownerId)
                .calculateCode(calculateCode)
                .totalAmount(totalAmount)
                .mobileAmount(mobileAmount)
                .creditCardAmount(creditCardAmount)
                .cashAmount(cashAmount)
                .build();
    }

    private SalesDto() {}

    public SalesDto(List<PaymentDto> payments){

    }

    public static Stream<SalesDto> toStream(List<PaymentDto> payments){
        return null;
    }
}
