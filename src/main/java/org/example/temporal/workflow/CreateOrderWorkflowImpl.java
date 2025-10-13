package org.example.temporal.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.example.temporal.activity.CreateOrderActivities;
import org.example.temporal.model.CreateOrderRequest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@WorkflowImpl(taskQueues = "order")
public class CreateOrderWorkflowImpl implements CreateOrderWorkflow {
    private final CreateOrderActivities activities = Workflow.newActivityStub(
            CreateOrderActivities.class,
            ActivityOptions.newBuilder().setScheduleToCloseTimeout(Duration.of(5, ChronoUnit.SECONDS)).build()
    );

    @Override
    public String createOrder(CreateOrderRequest request) {
        int rndNumber = Math.abs(Workflow.newRandom().nextInt());
        log.info("creatingOrder");
        activities.authorizePayment(request);
        activities.reserveInventory(request);
        activities.confirmOrder(request);
        activities.sendConfirmation(request);
        return "ORD-" + rndNumber;
    }
}
