package com.jojoldu.blogcode.querydsl.entityql.entity.scanner;

import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;

import java.util.Map;

/**
 * Created by jojoldu@gmail.com on 23/06/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface QEntityScannerFactory {

    QEntityScanner createScanner(Map<String, String> params) throws IllegalStateException;
}
