package com.blogcode.old;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2017-02-04
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
public class OldCompanyContract {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private double commission; // 수수료

    @Column(nullable = false)
    private String commissionType; // 수수료 타입 (예: 퍼센테이지, 금액)

    @Column(nullable = false)
    private String commissionCutting; // 수수료 절삭 (예: 반올림, 올림, 버림)

    public OldCompanyContract() {}

    public OldCompanyContract(String company, double commission, String commissionType, String commissionCutting) {
        this.company = company;
        this.commission = commission;
        this.commissionType = commissionType;
        this.commissionCutting = commissionCutting;
    }

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public double getCommission() {
        return commission;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public String getCommissionCutting() {
        return commissionCutting;
    }

}
