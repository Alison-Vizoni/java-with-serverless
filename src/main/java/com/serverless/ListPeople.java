package com.serverless;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListPeople implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    public static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().build();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
            DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
            List<Person> people = dynamoDBMapper.scan(Person.class, dynamoDBScanExpression);
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(people)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        } catch (Exception e) {
            e.getMessage();
            Response responseBody = new Response("Vada Pochey", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        }
    }
}
