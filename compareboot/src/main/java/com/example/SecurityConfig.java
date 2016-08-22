package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by jojoldu@gmail.com on 2016-08-22.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private ReaderRepository readerRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //security의 기본옵션이 csrf 토큰 필수로 되어있어, 현재 환경에선 불필요하여 off
                .authorizeRequests()
                .antMatchers("/").access("hasRole('READER')") // root path에는 READER 권한이 필요하다
                .antMatchers("/**").permitAll() // root외에 다른 path에는 권한이 필요없다

                .and()

                .formLogin()
                .loginPage("/login") //기본으로 잡혀지는 login 페이지의 url을 /login으로 지정한다
                .failureUrl("/login?error=true");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
            로그인을 시도한 사용자가 등록된 사용자인지 검증하는 설정
            JDBC, LDAP, 인메모리등의 저장소 옵션중 선택해서 사용한다
            여기선 JPA를 이용한 DB기반으로 인증
         */
        auth.userDetailsService(username -> readerRepository.findOne(username));
    }

}
