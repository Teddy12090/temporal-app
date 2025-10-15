package org.example.temporal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.Optional;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonSerialize()
public class FooRequest {
    int id;

    @NonNull
    String name;

    @JsonProperty
    String email;

    @JsonProperty
    String phone;

    @JsonProperty
    Address address;

    @JsonIgnore
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    @JsonIgnore
    public Optional<String> getPhone() {
        return Optional.ofNullable(phone);
    }

    @JsonIgnore
    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    @Builder
    @Value
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    public static class Address {
        String street;
        String city;
        @JsonProperty
        String postalCode;

        @JsonIgnore
        public Optional<String> getPostalCode() {
            return Optional.ofNullable(postalCode);
        }
    }
}
