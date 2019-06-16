package com.affirm.loans.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"interest_rate","amount","id","default_likelihood","state"})
public class Loan {
    private String loanId;
    private double amount;
    private double interestRate;
    private double defaultLikelihood;
    private String state;

    @JsonCreator
    public Loan(@JsonProperty("id") String id,
                @JsonProperty("state") String state,
                @JsonProperty("amount") String amount,
                @JsonProperty("interest_rate") String rate,
                @JsonProperty("default_likelihood") String defaultLikelihood) {
        this.loanId = id;
        this.state = state;
        this.amount = Double.parseDouble(amount);
        this.interestRate = Double.parseDouble(rate);
        this.defaultLikelihood = Double.parseDouble(defaultLikelihood);
    }

    @JsonProperty("id")
    public String getLoanId() {
        return loanId;
    }

    @JsonProperty("amount")
    public double getAmount() {
        return amount;
    }

    @JsonProperty("interest_rate")
    public double getInterestRate() {
        return interestRate;
    }

    @JsonProperty("default_likelihood")
    public double getDefaultLikelihood() {
        return defaultLikelihood;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }
}
