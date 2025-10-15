package org.example.temporal.workflow;

import io.temporal.workflow.Workflow;
import org.example.temporal.model.FooRequest;
import org.example.temporal.model.FooResponse;

public class OptionalWorkflowImpl implements OptionalWorkflow {
    @Override
    public FooResponse foo(FooRequest request) {
        Workflow.getLogger(OptionalWorkflowImpl.class).info(request.toString());
        return FooResponse.builder().build();
    }
}
