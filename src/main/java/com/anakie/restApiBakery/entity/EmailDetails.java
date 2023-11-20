package com.anakie.restApiBakery.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {

    private String recipientEmail;
    private String recipientUsername;
    private String msgBody;
    private String subject;
    private Order order;
    private Payment payment;
    private String attachment;

}
