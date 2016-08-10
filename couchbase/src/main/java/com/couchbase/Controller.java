package com.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jojoldu@gmail.com on 2016-08-10.
 */
@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping(value="/")
    public String home (Model model){
        model.addAttribute("hello", "Hello Couchbase + Spring boot");
        return "home";
    }

    @RequestMapping(value="/add")
    @ResponseBody
    public boolean add (){

        //Couchbase에 connection
        Cluster cluster = CouchbaseCluster.create("127.0.0.1");
        Bucket bucket = cluster.openBucket("default");

        /*
            Couchbase가 제공하는 JsonObject를 이용해서 Json 객체 생성
            Map이라고 봐도 무방하다
         */
        JsonObject json = JsonObject.create();
        json.put("name", "jojoldu");
        json.put("email", "jojoldu@gmail.com");
        json.put("homepage", "jojoldu.tistory.com");

        //저장
        String id = String.valueOf(Math.random()*10000+1);
        bucket.upsert(JsonDocument.create(id, json));

        return true;
    }
}
