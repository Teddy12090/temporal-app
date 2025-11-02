package org.example.temporal.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import org.example.temporal.model.FilterRequest;

@ActivityInterface
public interface FilterActivity {
    @ActivityMethod
    void filter(FilterRequest request);
}
