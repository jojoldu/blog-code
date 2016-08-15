package com.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jojoldu@gmail.com on 2016-08-14.
 */
@Configuration
public class Config {
    @Bean
    public Bucket bucket() {
        //Couchbase에 connection
        Cluster cluster = CouchbaseCluster.create("127.0.0.1");
        return cluster.openBucket("default");
    }
}
