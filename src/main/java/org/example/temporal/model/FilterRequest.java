package org.example.temporal.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@JsonDeserialize(builder = FilterRequest.FilterRequestBuilder.class)
public class FilterRequest {
    private String data1;
    private String data2;
    private Integer data3;
    private FilterRequest subRequest;
}
