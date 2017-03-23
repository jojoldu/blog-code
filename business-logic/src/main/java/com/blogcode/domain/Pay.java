package com.blogcode.domain;

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
public class Pay {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long ownerId; // 업주 Id

    @Column
    private LocalDate payDate; // 결제일

    @Column
    private String settleCode; // 정산방식을 선택하는 정산코드

    @Column
    @Enumerated(EnumType.STRING)
    private PayType type;

    @Column
    private int price;

    @Builder
    public Pay(Long ownerId, LocalDate payDate, String settleCode, PayType type, int price) {
        this.ownerId = ownerId;
        this.payDate = payDate;
        this.settleCode = settleCode;
        this.type = type;
        this.price = price;
    }

    public enum PayType {
        MOBILE("휴대폰"),
        CREDIT_CARD("신용카드"),
        CASH("현금");

        private String text;

        PayType(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }



}
