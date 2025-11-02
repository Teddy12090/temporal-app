package org.example.temporal.config;

import com.google.gson.GsonBuilder;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.common.converter.GsonJsonPayloadConverter;
import io.temporal.spring.boot.TemporalOptionsCustomizer;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class GsonConfig implements TemporalOptionsCustomizer<WorkflowClientOptions.Builder> {
    @Nonnull
    @Override
    public WorkflowClientOptions.Builder customize(@Nonnull WorkflowClientOptions.Builder optionsBuilder) {
        return optionsBuilder.setDataConverter(DefaultDataConverter
                .newDefaultInstance()
                .withPayloadConverterOverrides(new GsonJsonPayloadConverter(_ -> new GsonBuilder())));
    }
}
