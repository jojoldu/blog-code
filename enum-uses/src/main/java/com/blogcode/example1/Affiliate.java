package com.blogcode.example1;

import lombok.Getter;

import javax.persistence.*;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
@Getter
public class Affiliate {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String representative;

    @Column
    private String address;

    @Column
    @Enumerated(EnumType.STRING)
    private Code code;

    public enum Code {
        WOOWA_SISTERS("우아한자매들", "w01", "s0001"),
        WOOWA_CHILDREN("우아한아이들", "w02", "s0099"),
        WOOWA_ADULTS("우아한어른들", "w03", "s1201");

        private String viewName;
        private String companyCode;
        private String bizTypeCode;

        Code(String viewName, String companyCode, String bizTypeCode) {
            this.viewName = viewName;
            this.companyCode = companyCode;
            this.bizTypeCode = bizTypeCode;
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
    }
}
