package org.example.temporal.activity;

import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.temporal.model.CreateOrderRequest;
import org.springframework.stereotype.Component;

@Slf4j
@ActivityImpl(taskQueues = "order")
@Component
public class CreateOrderActivitiesImpl implements CreateOrderActivities {
    @Override
    public void authorizePayment(CreateOrderRequest request) {
        log.info("authorizePayment");
    }

    @Override
    public void reserveInventory(CreateOrderRequest request) {
        log.info("reserveInventory");
    }

    @Override
    public void confirmOrder(CreateOrderRequest request) {
        log.info("confirmOrder");
    }

    @Override
    public void sendConfirmation(CreateOrderRequest request) {
        log.info("sendConfirmation");
    }
}
