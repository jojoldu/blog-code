package com.jojoldu.blogcode;

import org.springframework.test.context.TestPropertySource;


@TestPropertySource(properties = "embedded-dynamodb.use=true")
public interface EmbeddedDynamoDbTest {
}
