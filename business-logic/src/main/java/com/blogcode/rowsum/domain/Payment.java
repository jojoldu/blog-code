package com.blogcode.rowsum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 22.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long ownerId; // 업주 Id

    @Column
    private LocalDate payDate; // 결제일

    @Column
    private String calculateCode; // 계산방식을 선택하는 계산코드

    @Column
    @Enumerated(EnumType.STRING)
    private Method method; // 결제방식

    @Column
    private int price;

    @Builder
    public Payment(Long ownerId, LocalDate payDate, String calculateCode, Method method, int price) {
        this.ownerId = ownerId;
        this.payDate = payDate;
        this.calculateCode = calculateCode;
        this.method = method;
        this.price = price;
    }

    public enum Method {
        MOBILE("휴대폰"),
        CREDIT_CARD("신용카드"),
        CASH("현금");

        private String text;

        Method(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
