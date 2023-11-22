package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Order;
import com.anakie.restApiBakery.entity.Payment;

public interface RecordService {

    String invoice(Order order);

}
