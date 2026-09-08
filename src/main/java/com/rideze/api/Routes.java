package com.rideze.api;

public final class Routes {

    private Routes() {}

    // Auth routes
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_VERIFY_OTP = "/auth/verify-otp";

    // Toll Claims routes
    public static final String GET_TOLL_CLAIMS = "/tolls/claims";
    public static final String GET_TOLL_CLAIM_BY_ID = "/tolls/claims/{claimId}";
    public static final String APPROVE_TOLL_CLAIM = "/tolls/claims/{claimId}/approve";
    public static final String REJECT_TOLL_CLAIM = "/tolls/claims/{claimId}/reject";

    // Fleet & Review routes
    public static final String GET_VEHICLES = "/vehicles";
    public static final String GET_REVIEWS = "/reviews";

    // Booking & Customer routes
    public static final String GET_BOOKINGS = "/bookings";
    public static final String GET_BOOKING_BY_ID = "/bookings/{bookingId}";

    // Payment routes (Razorpay)
    public static final String RAZORPAY_CREATE_ORDER = "/payments/razorpay/create-order";
    public static final String RAZORPAY_VERIFY = "/payments/razorpay/verify";
}