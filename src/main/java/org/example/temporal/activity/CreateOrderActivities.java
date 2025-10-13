package org.example.temporal.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import org.example.temporal.model.CreateOrderRequest;

@ActivityInterface
public interface CreateOrderActivities {
    @ActivityMethod
    void authorizePayment(CreateOrderRequest request);

    @ActivityMethod
    void reserveInventory(CreateOrderRequest request);

    @ActivityMethod
    void confirmOrder(CreateOrderRequest request);

    @ActivityMethod
    void sendConfirmation(CreateOrderRequest request);
}
