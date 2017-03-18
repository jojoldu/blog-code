package com.blogcode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 18.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */


@Configuration
public class ProxyConfiguration {

    @Bean
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Proxy proxy() {
        return new ProxyImpl();
    }
}