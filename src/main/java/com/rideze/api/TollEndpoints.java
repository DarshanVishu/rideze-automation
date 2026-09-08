package com.rideze.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public final class TollEndpoints {

    private TollEndpoints() {}

    public static Response getPendingTollClaims(RequestSpecification spec) {
        return RestAssured.given()
                .spec(spec)
                .queryParam("status", "PENDING")
                .when()
                .get(Routes.GET_TOLL_CLAIMS);
    }

    public static Response getClaimById(RequestSpecification spec, String claimId) {
        return RestAssured.given()
                .spec(spec)
                .pathParam("claimId", claimId)
                .when()
                .get(Routes.GET_TOLL_CLAIM_BY_ID);
    }

    public static Response approveTollClaim(RequestSpecification spec, String claimId, String remarks) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "APPROVED");
        body.put("remarks", remarks);

        return RestAssured.given()
                .spec(spec)
                .pathParam("claimId", claimId)
                .body(body)
                .when()
                .put(Routes.APPROVE_TOLL_CLAIM);
    }

    public static Response rejectTollClaim(RequestSpecification spec, String claimId, String reason) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "REJECTED");
        body.put("reason", reason);

        return RestAssured.given()
                .spec(spec)
                .pathParam("claimId", claimId)
                .body(body)
                .when()
                .put(Routes.REJECT_TOLL_CLAIM);
    }
}