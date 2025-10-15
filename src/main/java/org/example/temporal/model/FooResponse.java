package org.example.temporal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Optional;

import static org.example.temporal.model.FooRequest.Address;

@Value
@NoArgsConstructor(force = true)
@Builder
@AllArgsConstructor
public class FooResponse {
    int id;

    @JsonProperty
    String phone;

    @JsonProperty
    Address address;

    @JsonIgnore
    public Optional<String> getPhone() {
        return Optional.ofNullable(phone);
    }

    @JsonIgnore
    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }
}
