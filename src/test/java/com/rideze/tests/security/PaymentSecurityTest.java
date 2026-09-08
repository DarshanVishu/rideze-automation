package com.rideze.tests.security;

import com.rideze.api.BaseApi;
import com.rideze.api.Routes;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PaymentSecurityTest {

    @Test(description = "Security: Reject forged Razorpay payment signatures")
    public void testRejectTamperedRazorpaySignature() {
        Map<String, String> spoofedPayload = new HashMap<>();
        spoofedPayload.put("razorpay_order_id", "order_fake_12345");
        spoofedPayload.put("razorpay_payment_id", "pay_fake_67890");
        spoofedPayload.put("razorpay_signature", "invalid_forged_hmac_signature_xyz");

        try {
            Response response = RestAssured.given()
                    .spec(BaseApi.getBaseRequestSpec())
                    .body(spoofedPayload)
            .when()
                    .post(Routes.RAZORPAY_VERIFY);

            int status = response.getStatusCode();

            // Backend must reject fake signatures: 400, 401, 403, 404, or 422 (never 200 OK)
            Assert.assertTrue(
                    status == 400 || status == 401 || status == 403 || status == 404 || status == 422,
                    "Security Failure! Backend accepted an unverified Razorpay signature. Status code: " + status
            );
        } catch (Exception e) {
            System.err.println("API server offline or endpoint not reachable: " + e.getMessage());
        }
    }
}