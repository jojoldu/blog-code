package com.jojoldu.blogcode.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.jojoldu.blogcode.dynamodb.Order.DYNAMO_TABLE_NAME;

@Getter
@Setter
@NoArgsConstructor
@DynamoDBTable(tableName = DYNAMO_TABLE_NAME)
public class Order {
    public static final String DYNAMO_TABLE_NAME = "Order";

    private String orderNo;
    private Long orderAmount;

}
