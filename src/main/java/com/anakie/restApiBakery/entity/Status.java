package com.anakie.restApiBakery.entity;

public enum Status {

    // Payment Statuses:
    Pending, // Payment/Order: Payment has been initiated but not completed.
    Successful, // Payment: Payment has been successfully processed.
    Failed,  //Payment: The payment transaction was unsuccessful.
    Refunded, // Payment was reversed, and the amount returned to the user.
    Partially, // Paid: Only a portion of the total amount has been paid.



    // Order Statuses:
    Confirmed, // Order has been confirmed and is pending processing.
    In_Progress, // Order is currently being processed.
    Delivered, // Order has been successfully delivered to the customer.
     // Account Statuses:
    Active, // The account is active and in good standing.
    Inactive, //The account is temporarily inactive.
    Funded,
     Closed, // The account has been permanently closed.
    Suspended, // Account activity has been suspended, possibly due to a violation of terms.
    Frozen, // Account transactions are temporarily restricted.

    Paid_With_Account,
    Got_Change
}
