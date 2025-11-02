package org.example.temporal.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.example.temporal.model.FilterRequest;

@WorkflowInterface
public interface FilterWorkflow {
    @WorkflowMethod
    String filter(FilterRequest request);
}
