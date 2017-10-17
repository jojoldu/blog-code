package com.jojoldu.h2.domain;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 12.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Configuration
@Profile("local")
public class H2ServerConfiguration {

    @Bean
    public Server h2TcpServer() throws SQLException {
        Server h2Server = Server.createTcpServer().start();
        if (h2Server.isRunning(true)) {
            System.out.println("H2 server was started and is running.");
            return h2Server;
        } else {
            throw new RuntimeException("Could not start H2 server.");
        }
    }
}
