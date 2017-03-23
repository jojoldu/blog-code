package com.blogcode.dto;

import com.blogcode.domain.Payment;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 23.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Getter
@EqualsAndHashCode(exclude = {"id", "paymentMethod", "price"})
public class PaymentDto {
    private Long id;
    private Long ownerId; // 업주 Id
    private LocalDate payDate; // 결제일
    private String calculateCode; // 계산방식을 선택하는 계산코드
    private Payment.Method paymentMethod; // 결제방식
    private int price;

    public PaymentDto(Payment entity) {
        this.id = entity.getId();
        this.ownerId = entity.getOwnerId();
        this.payDate = entity.getPayDate();
        this.calculateCode = entity.getCalculateCode();
        this.paymentMethod = entity.getMethod();
        this.price = entity.getPrice();
    }

    public static Stream<PaymentDto> toStream(List<Payment> payments){
        return payments.stream()
                .map(PaymentDto::new);
    }
}
