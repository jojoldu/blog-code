package com.jojoldu.blogcode.springboot.tips.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestSetterDto {
    private String name;
    private Long amount;
}
