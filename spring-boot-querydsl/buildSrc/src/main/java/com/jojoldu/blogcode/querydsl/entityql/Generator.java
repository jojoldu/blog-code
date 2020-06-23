package com.jojoldu.blogcode.querydsl.entityql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jojoldu@gmail.com on 23/06/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ToString
@Setter
@Getter
@NoArgsConstructor
public class Generator {
    enum Type {
        JPA
    }

    private static final String SEPARATOR = System.getProperty("file.separator");

    private Type type = Type.JPA;

    private Map<String, String> params = new HashMap<>();

    private String sourcePackage;

    private String destinationPackage;

    private String filenamePattern = "E%s.java";

    private String destinationPath;

    void setDefaultDestinationPathIfNeeded(String baseDir) {
        if (destinationPath == null) {
            destinationPath = baseDir + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "java";
        }
    }
}
