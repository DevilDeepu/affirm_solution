package com.affirm.loans.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"loan_id","facility_id"})
public class Assignment {
    private String facilityId;
    private String loanId;

    @JsonCreator
    public Assignment(@JsonProperty("loan_id") String lid, @JsonProperty("facility_id") String fid) {
        this.facilityId = fid;
        this.loanId = lid;
    }

    @JsonProperty("facility_id")
    public String getFacilityId() {
        return facilityId;
    }

    @JsonProperty("loan_id")
    public String getLoanId() {
        return loanId;
    }
}
