package org.example.temporal.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.example.temporal.model.CreateOrderRequest;

@WorkflowInterface
public interface CreateOrderWorkflow {
    @WorkflowMethod
    String createOrder(CreateOrderRequest request);
}
