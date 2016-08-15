package com.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-08-10.
 */
@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private Bucket bucket;

    @RequestMapping(value="/")
    public String home (Model model, HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("hello", "Hello Couchbase + Spring boot");
        model.addAttribute("uid", CouchBaseSession.getUid(request, response));
        return "home";
    }

    @RequestMapping(value = "/user")
    @ResponseBody
    public Object get(HttpServletRequest request, HttpServletResponse response){
        return bucket.get(CouchBaseSession.getUid(request, response)).content();
    }

    @RequestMapping(value="/user/all")
    @ResponseBody
    public List<User> getAll(HttpServletRequest request, HttpServletResponse response) {

    }

    @RequestMapping(value="/user", method = RequestMethod.POST)
    @ResponseBody
    public boolean add (HttpServletRequest request, HttpServletResponse response, User user){
        /*
            Couchbase가 제공하는 JsonObject를 이용해서 Json 객체 생성
            Map이라고 봐도 무방하다
         */
        JsonObject json = JsonObject.create();
        json.put("type", "user_info");
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("homepage", user.getHomepage());

        //저장
        bucket.upsert(JsonDocument.create(CouchBaseSession.getUid(request, response), json));

        return true;
    }
}
