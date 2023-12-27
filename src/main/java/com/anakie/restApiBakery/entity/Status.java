package com.anakie.restApiBakery.entity;

public enum Status {

    // Payment Statuses:
    Pending, // Payment/Order: Payment has been initiated but not completed.
    Successful, // Payment: Payment has been successfully processed.
    Failed,  //Payment: The payment transaction was unsuccessful.
    Refunded, // Payment was reversed, and the amount returned to the user.
    Partially, // Paid: Only a portion of the total amount has been paid.



    // Order Statuses:

    Processing, // Order is currently being processed.
    Preparing, // after payment, we prepare the order
    Delivering, // we then deliver the order
    Delivered, // Order has been successfully delivered to the customer.

     // Account Statuses:
    Funded,  // when a users directly funds it So they can pay with app account cash
    Paid_With_Account, // when we pay using an app account
    Got_Change // when they receive change
    ,
    New
}
