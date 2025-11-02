package org.example.temporal.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.example.temporal.activity.FilterActivity;
import org.example.temporal.model.FilterRequest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@WorkflowImpl(taskQueues = "filter")
public class FilterWorkflowImpl implements FilterWorkflow {
    private final FilterActivity filterActivity = Workflow.newActivityStub(FilterActivity.class,
            ActivityOptions.newBuilder().setScheduleToCloseTimeout(Duration.of(5, ChronoUnit.SECONDS)).build());

    @Override
    public String filter(FilterRequest request) {
        filterActivity.filter(request);
        filterActivity.filter(request.toBuilder().data1(" ").data2("foo").build());
        filterActivity.filter(request.toBuilder().data1(null).data2("foo").build());
        return "";
    }
}
