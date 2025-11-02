package org.example.temporal.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.common.converter.JacksonJsonPayloadConverter;
import io.temporal.spring.boot.TemporalOptionsCustomizer;

import javax.annotation.Nonnull;

//@Component
public class JacksonConfig implements TemporalOptionsCustomizer<WorkflowClientOptions.Builder> {
    @Nonnull
    @Override
    public WorkflowClientOptions.Builder customize(@Nonnull WorkflowClientOptions.Builder optionsBuilder) {
        ObjectMapper mapper = JacksonJsonPayloadConverter.newDefaultObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        return optionsBuilder.setDataConverter(DefaultDataConverter
                .newDefaultInstance()
                .withPayloadConverterOverrides(new JacksonJsonPayloadConverter(mapper)));
    }
}
