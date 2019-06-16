package com.affirm.loans.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","name"})
public class Bank {
    private String bankId;
    private String name;

    @JsonCreator
    public Bank(@JsonProperty("id") String id, @JsonProperty("name") String name) {
        this.bankId = id;
        this.name = name;
    }

    @JsonProperty("id")
    public String getBankId() {
        return bankId;
    }

    @JsonProperty("name")
    public String getBankName() {
        return name;
    }
}
