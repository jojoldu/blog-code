package com.blogcode.jpa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class Academy {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private List<Subject> subjects = new ArrayList<>();


}
