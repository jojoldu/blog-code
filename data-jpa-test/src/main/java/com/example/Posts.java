package com.example;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by jojoldu@gmail.com on 2016-08-28.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idx;


    @Column
    private LocalDate updateDate;
}
