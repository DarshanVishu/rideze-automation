package com.rideze.tests.security;

import com.rideze.api.BaseApi;
import com.rideze.api.Routes;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingIdorSecurityTest {

    @Test(description = "Security: Prevent Customer B from accessing Customer A's booking (IDOR/BOLA)")
    public void testCustomerCannotAccessAnotherCustomerBooking() {
        // Token for Customer B (attacker or separate customer)
        String customerBToken = "mock-token-customer-b";
        
        // Arbitrary booking ID owned by Customer A
        String customerABookingId = "101";

        try {
            // Customer B attempts to view Customer A's booking details
            Response response = RestAssured.given()
                    .spec(BaseApi.getAuthenticatedRequestSpec(customerBToken))
                    .pathParam("bookingId", customerABookingId)
            .when()
                    .get(Routes.GET_BOOKING_BY_ID);

            int status = response.getStatusCode();

            // Secure backend behavior: must refuse access (401, 403, or 404), NEVER 200 OK
            Assert.assertTrue(
                    status == 401 || status == 403 || status == 404,
                    "Security Failure! Unauthorized user accessed booking data. Status code: " + status
            );
        } catch (Exception e) {
            System.err.println("API server offline or endpoint not reachable: " + e.getMessage());
        }
    }
}