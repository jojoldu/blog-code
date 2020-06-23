package com.jojoldu.blogcode.querydsl.entityql;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 23/06/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public class GeneratorTest {

    @Test
    void source_package_set_get() throws Exception {
        //given
        Generator generator = new Generator();
        String sourcePackage = "jojoldu";

        //when
        generator.setSourcePackage(sourcePackage);
        String result = generator.getSourcePackage();

        //then
        assertThat(result).isEqualTo(sourcePackage);
    }
}
