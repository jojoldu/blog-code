package com.jojoldu.blogcode.querydsl.entityql.entity.scanner;

/**
 * Created by jojoldu@gmail.com on 23/06/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
import pl.exsio.querydsl.entityql.entity.scanner.JpaQEntityScanner;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;

import java.util.Map;

public class JPAQEntityScannerFactory implements QEntityScannerFactory {

    @Override
    public QEntityScanner createScanner(Map<String, String> params) {
        return new JpaQEntityScanner();
    }
}
