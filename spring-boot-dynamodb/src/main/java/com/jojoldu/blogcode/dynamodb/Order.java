package com.jojoldu.blogcode.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

import static com.jojoldu.blogcode.dynamodb.Order.DYNAMO_TABLE_NAME;

@Getter
@Setter
@NoArgsConstructor
@DynamoDBTable(tableName = DYNAMO_TABLE_NAME)
public class Order {
    public static final String DYNAMO_TABLE_NAME = "Order";

    @Id
    @DynamoDBHashKey
    private String orderNo;

    @DynamoDBAttribute
    private Long orderAmount;

    @Builder
    public Order(String orderNo, Long orderAmount) {
        this.orderNo = orderNo;
        this.orderAmount = orderAmount;
    }
}
