package com.affirm.loans.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"facility_id","expected_yield"})
public class Yield {
    private String facilityId;
    private double expectedYield;

    @JsonCreator
    public Yield(@JsonProperty("facility_id") String fid,
                @JsonProperty("expected_yield") String yield) {
        this.facilityId = fid;
        this.expectedYield = Double.parseDouble(yield);
    }

    public Yield(String fid, Double yield) {
        this.facilityId = fid;
        this.expectedYield = yield;
    }

    @JsonProperty("facility_id")
    public String getFacilityId() {
        return facilityId;
    }

    @JsonProperty("expected_yield")
    public double getExpectedYield() {
        return expectedYield;
    }
}
