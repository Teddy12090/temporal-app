package org.example.temporal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@Builder
@AllArgsConstructor
public class FooResponse {
    int id;
}
