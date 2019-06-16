package com.affirm.loans;

import com.affirm.loans.datasource.CsvDataSource;
import com.affirm.loans.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class LoanAllocationService {
    private LoanAllocationServiceConfig config;

    private Map<String, Bank> banks;
    private List<Facility> facilities;
    private List<Covenant> covenants;
    private List<Loan> loans;

    private List<Assignment> loanAssignments;

    public LoanAllocationService(String propertiesFile) throws IOException {
        config = new LoanAllocationServiceConfig(propertiesFile);
    }

    public void readData() throws IOException {
        try {
            System.out.println("Loading Bank records");
            List<Bank> rawBankRecs = CsvDataSource.readRecords(config.bankDataSource(), Bank.class);
            banks = rawBankRecs.stream().collect(Collectors.toMap(b-> b.getBankId(), b-> b));
        }
        catch (IOException ex) {
            System.out.println("Error reading Bank records");
            throw new IOException(ex);
        }

        try {
            System.out.println("Loading Bank Facility Covenants records");
            covenants = CsvDataSource.readRecords(config.covenantDataSource(), Covenant.class);
        }
        catch (IOException ex) {
            System.out.println("Error reading Covenants records");
            throw new IOException(ex);
        }

        try {
            System.out.println("Loading Bank Facilities records");
            facilities = CsvDataSource.readRecords(config.facilityDataSource(), Facility.class);
            facilities.forEach(fac-> fac.addFacilityCovenant(covenants.stream()
                                            .filter(covenant -> covenant.getFacilityId().equals(fac.getFacilityId()))
                                            .collect(Collectors.toList())
                    )
            );

            Collections.sort(facilities, Comparator.comparingDouble(fac-> fac.getInterestRate()));
        }
        catch (IOException ex) {
            System.out.println("Error reading Facility records");
            throw new IOException(ex);
        }

        try {
            System.out.println("Loading loan records");
            loans = CsvDataSource.readRecords(config.loanDataSource(), Loan.class);
        }
        catch (IOException ex) {
            System.out.println("Error reading Loan records");
            throw new IOException(ex);
        }
    }

    public void processLoanAssigments() {
        Objects.requireNonNull(banks, "Missing Banks records");
        Objects.requireNonNull(facilities, "Missing Facilities records");
        Objects.requireNonNull(covenants, "Missing Facility Covenant records");
        Objects.requireNonNull(loans, "Missing Loan records");

        System.out.println("Processing loan data stream");
        loanAssignments = loans.stream().map(loan-> {
            Optional<Facility> candidate = facilities.stream()
                    .filter(fac-> fac.isLoanEligible(loan) )
                    .findFirst();

            if( candidate.isPresent() ) {
                candidate.get().assignLoan(loan);
                return new Assignment(loan.getLoanId(), candidate.get().getFacilityId());
            }
            else
                return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        System.out.println("Done generating loan assignments.");
    }

    public void persistResults() throws IOException {
        try {
            System.out.println("Generating loan assignments record file.");
            CsvDataSource.writeRecords(config.assignmentDataSource(),
                    Assignment.class, loanAssignments);
        }
        catch (IOException ex) {
            System.out.println("Error generating assignments file");
            throw new IOException(ex);
        }

        try {
            System.out.println("Generating expected yield record file.");
            CsvDataSource.writeRecords(config.yieldDataSource(),
                    Yield.class, facilities.stream()
                            .map(x-> new Yield(x.getFacilityId(), x.getExpectedYield())).collect(Collectors.toList()));
        }
        catch (IOException ex) {
            System.out.println("Error generating yields file");
            throw new IOException(ex);
        }
    }

    public static void main(String[] args) {
        try {
            if( args.length < 1 )
                throw new IllegalArgumentException("Program argument should be a config file name");

            LoanAllocationService loanServicing = new LoanAllocationService(args[0]);

            loanServicing.readData();

            loanServicing.processLoanAssigments();

            loanServicing.persistResults();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
