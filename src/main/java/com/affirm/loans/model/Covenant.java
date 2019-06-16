package com.affirm.loans.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"facility_id","max_default_likelihood","bank_id","banned_state"})
public class Covenant {
    private String facilityId;
    private String bankId;
    private double maxDefaultLikelihood;
    private String bannedState;

    @JsonCreator
    public Covenant(@JsonProperty("facility_id") String fid,
                    @JsonProperty("max_default_likelihood") String defaultLikelihood,
                    @JsonProperty("bank_id") String bid,
                    @JsonProperty("banned_state") String state) {
        this.facilityId = fid;
        this.bankId = bid;
        this.maxDefaultLikelihood = defaultLikelihood == null || defaultLikelihood.isEmpty() ? 0 : Double.parseDouble(defaultLikelihood);
        this.bannedState = state;
    }

    @JsonProperty("facility_id")
    public String getFacilityId() {
        return facilityId;
    }

    @JsonProperty("bank_id")
    public String getBankId() {
        return bankId;
    }

    @JsonProperty("banned_state")
    public String getBannedState() {
        return bannedState;
    }

    @JsonProperty("max_default_likelihood")
    public double getMaxDefaultLikelihood() {
        return maxDefaultLikelihood;
    }

    public boolean isEligible(Loan loan) {
        return ! this.getBannedState().equals(loan.getState()) &&
                loan.getDefaultLikelihood() <= this.getMaxDefaultLikelihood();
    }
}

