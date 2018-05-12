package com.jojoldu.blogcode.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@SpringBootApplication
public class Application {

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        redisServer = new RedisServer();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
