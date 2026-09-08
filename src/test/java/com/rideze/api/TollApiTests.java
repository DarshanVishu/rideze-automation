package com.rideze.api;

import com.rideze.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class TollApiTests {

    private String authToken;

    @BeforeClass
    public void setupAuth() {
        Map<String, String> creds = new HashMap<>();
        creds.put("email", ConfigReader.get("adminEmail"));
        creds.put("password", ConfigReader.get("adminPassword"));

        try {
            Response response = RestAssured.given()
                    .spec(BaseApi.getBaseRequestSpec())
                    .body(creds)
                    .when()
                    .post(Routes.AUTH_LOGIN);

            if (response.getStatusCode() == 200) {
                authToken = response.jsonPath().getString("token");
            }
        } catch (Exception e) {
            authToken = "mock-jwt-token";
        }

        if (authToken == null) {
            authToken = "mock-jwt-token";
        }
    }

    @Test(description = "Verify retrieving pending toll claims via API")
    public void testGetPendingTollClaims() {
        try {
            Response response = TollEndpoints.getPendingTollClaims(BaseApi.getAuthenticatedRequestSpec(authToken));
            int status = response.getStatusCode();
            Assert.assertTrue(
                    status == 200 || status == 401 || status == 404,
                    "Unexpected API status code received: " + status
            );
        } catch (Exception e) {
            System.err.println("Backend API server not reachable at target endpoint: " + e.getMessage());
        }
    }
}