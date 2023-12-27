package com.anakie.restApiBakery.service;


import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.entity.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {

    Map<Long, Integer> mostSoldProducts(LocalDate fromDate, LocalDate toDate);

    Map<Long, Integer> mostSoldCategories(LocalDate fromDate, LocalDate toDate);

    List<Order> ordersPlaced(List<LocalDate> dates);

    List<Order> outstandingOrders(List<LocalDate> dates);

    List<Order> delivered(List<LocalDate> dates);

    List<Ingredient> ingredientsToOrder();

    List<Ingredient> stockIngredients();


}
