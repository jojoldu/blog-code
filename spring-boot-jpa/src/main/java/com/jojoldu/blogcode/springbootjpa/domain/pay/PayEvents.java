package com.jojoldu.blogcode.springbootjpa.domain.pay;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 30/03/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Getter
@NoArgsConstructor
@Embeddable
public class PayEvents {
    public static final PayEvents EMPTY = new PayEvents();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pay")
    private List<PayEvent> payEvents = new ArrayList<>();

    public void add (Pay pay, PayEvent payDetail) {
        this.payEvents.add(payDetail);
        payDetail.setPay(pay);
    }

    public PayEvent get(int index) {
        return payEvents.get(index);
    }
}
