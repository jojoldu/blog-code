package com.example;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by jojoldu@gmail.com on 2016-08-22.
 */
public class ReaderHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        /*
            파라미터 타입이 reader일 경우 아래 resolveArgument를 실행한다
         */
        return Reader.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication auth = (Authentication) webRequest.getUserPrincipal();
        return auth != null && auth.getPrincipal() instanceof Reader ? auth.getPrincipal() : null;
    }

}
