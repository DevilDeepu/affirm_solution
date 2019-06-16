package com.affirm.loans.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"amount","interest_rate","id","bank_id"})
public class Facility {
    private String facilityId;
    private String bankId;
    private double interestRate;
    private double amount;

    @JsonIgnore
    private double allocatedAmount;
    @JsonIgnore
    private List<Covenant> covenants = new ArrayList<>();
    @JsonIgnore
    private Double yield = 0.0;

    @JsonCreator
    public Facility(@JsonProperty("id") String fid,
                    @JsonProperty("bank_id") String bid,
                    @JsonProperty("amount") String amount,
                    @JsonProperty("interest_rate") String rate) {
        this.facilityId = fid;
        this.bankId = bid;
        this.amount = Double.parseDouble(amount);
        this.interestRate = Double.parseDouble(rate);
    }

    @JsonProperty("facility_id")
    public String getFacilityId() {
        return facilityId;
    }

    @JsonProperty("bank_id")
    public String getBankId() {
        return bankId;
    }

    @JsonProperty("interest_rate")
    public double getInterestRate() {
        return interestRate;
    }

    @JsonProperty("amount")
    public double getAmount() {
        return amount;
    }

    public void assignLoan(Loan loan) {
        this.allocatedAmount += loan.getAmount();
        this.yield += ( ( 1 - loan.getDefaultLikelihood() ) * loan.getInterestRate() * loan.getAmount() ) -
                ( loan.getDefaultLikelihood() * loan.getAmount() ) -
                ( this.getInterestRate() * loan.getAmount() );
    }

    public void addFacilityCovenant(Covenant covenant) {
        this.covenants.add( covenant );
    }

    public void addFacilityCovenant(List<Covenant> covenants) {
        this.covenants.addAll( covenants );
    }

    private boolean hasCapacity(double loanAmt) {
        return loanAmt <= (this.getAmount()-this.allocatedAmount);
    }
    public boolean isLoanEligible(Loan loan) {
        return this.covenants.stream().anyMatch(x-> x.isEligible(loan) && hasCapacity(loan.getAmount()));
    }

    public double getExpectedYield() {
        return yield;
    }
}
