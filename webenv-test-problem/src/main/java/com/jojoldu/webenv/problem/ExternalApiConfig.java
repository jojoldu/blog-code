package com.jojoldu.webenv.problem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Created by jojoldu@gmail.com on 2017. 11. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix="api.external")
public class ExternalApiConfig {

//    @NotNull
    private String host;
//    @NotNull
    private String url;
//    @NotNull
    private String type;
}
