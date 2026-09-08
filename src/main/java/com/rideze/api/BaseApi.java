package com.rideze.api;

import com.rideze.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApi {

    protected static RequestSpecification getBaseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("baseUrl") + "/api/v1")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    protected static RequestSpecification getAuthenticatedRequestSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseRequestSpec())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
}