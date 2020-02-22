package com.jojoldu.blogcode.springboot.tips;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.parse;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class LocalDateTimeUtils {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

    public static LocalDateTime toLocalDateTime (String dateTime) {
        try {
            return parse(dateTime, FORMATTER);
        } catch (Exception e) {
            String message = "입력 받은 date 문자열 parse에 실패했습니다. dateTime=" + dateTime;
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public static String toStringDateTime (LocalDateTime localDateTime) {
        try {
            return FORMATTER.format(localDateTime);
        } catch (Exception e) {
            String message = "입력 받은 LocalDateTime parse에 실패했습니다. dateTime=" + localDateTime;
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
