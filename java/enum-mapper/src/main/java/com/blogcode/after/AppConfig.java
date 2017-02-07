package com.blogcode.after;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Configuration
public class AppConfig {

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("commissionType", EnumContract.CommissionType.class);
        enumMapper.put("commissionCutting", EnumContract.CommissionCutting.class);
        return enumMapper;
    }
}
