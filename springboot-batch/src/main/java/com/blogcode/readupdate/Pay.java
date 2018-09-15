package com.blogcode.readupdate;

/**
 * Created by jojoldu@gmail.com on 2018. 9. 15.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private boolean isSuccess;

    public Pay(Long amount, boolean isSuccess) {
        this.amount = amount;
        this.isSuccess = isSuccess;
    }

    public void success() {
        this.isSuccess = true;
    }
}
