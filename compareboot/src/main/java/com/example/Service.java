package com.example;

/**
 * Created by jojoldu@gmail.com on 2016-08-21.
 */
@org.springframework.stereotype.Service
public class Service {

    public int getDiceNumber(){
        return (int)(Math.random()*6) +1;
    }
}
