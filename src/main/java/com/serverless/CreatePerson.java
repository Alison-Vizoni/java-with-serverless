package com.serverless;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Map;

public class CreatePerson implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    public static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().build();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            Person person = new Person();
            person.setName(body.get("name").asText());

            DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
            dynamoDBMapper.save(person);

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(person)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        } catch (Exception e){
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
