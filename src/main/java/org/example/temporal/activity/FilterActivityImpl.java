package org.example.temporal.activity;

import io.temporal.spring.boot.ActivityImpl;
import org.example.temporal.model.FilterRequest;
import org.springframework.stereotype.Component;

@Component
@ActivityImpl(taskQueues = "filter")
public class FilterActivityImpl implements FilterActivity {
    @Override
    public void filter(FilterRequest request) {

    }
}
