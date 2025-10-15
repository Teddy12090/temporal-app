package org.example.temporal.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.example.temporal.model.FooRequest;
import org.example.temporal.model.FooResponse;

@WorkflowInterface
public interface OptionalWorkflow {
    @WorkflowMethod
    FooResponse foo(FooRequest request);
}
