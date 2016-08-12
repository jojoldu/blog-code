package com.couchbase;

import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by jojoldu@zuminternet.com on 2016-08-12.
 */
public class SessionUtil {

    public static String generateUid(HttpServletResponse response){
        String uid = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("UID", uid);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        response.addCookie(cookie);

        return uid;
    }

    public static String getUid(HttpServletRequest request, HttpServletResponse response){
        String uid = Arrays.asList(request.getCookies())
                .stream()
                .filter(cookie -> "UID".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");

        if(StringUtils.isEmpty(uid)){
            uid = generateUid(response);
        }

        return uid;
    }

}
