package com.jojoldu.blogcode.querydsl.domain.academy.bulk;


import com.jojoldu.blogcode.querydsl.domain.academy.Academy;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AcademyUniqueKeyDto {
    private Long id;
    private String matchKey;

    public Academy toEntity() {
        return new Academy(id, matchKey);
    }
}
