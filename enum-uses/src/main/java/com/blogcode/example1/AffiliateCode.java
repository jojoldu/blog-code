package com.blogcode.example1;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 7.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public enum AffiliateCode {
    WOOWA_SISTERS("우아한자매들", "w01", "s0001", "123-45-6789"),
    WOOWA_CHILDREN("우아한아이들", "w02", "s0099", "001-02-0003"),
    WOOWA_ADULTS("우아한어른들", "w03", "s1201", "987-65-4321");

    private String viewName;
    private String companyCode;
    private String bizTypeCode;
    private String bizNumber;

    AffiliateCode(String viewName, String companyCode, String bizTypeCode, String bizNumber) {
        this.viewName = viewName;
        this.companyCode = companyCode;
        this.bizTypeCode = bizTypeCode;
        this.bizNumber = bizNumber;
    }

    public String getViewName() {
        return viewName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getBizTypeCode() {
        return bizTypeCode;
    }

    public String getBizNumber() {
        return bizNumber;
    }
}
