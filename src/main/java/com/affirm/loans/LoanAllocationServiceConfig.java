package com.affirm.loans;

import com.affirm.loans.datasource.CsvDataSource;
import com.affirm.loans.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class LoanAllocationServiceConfig {
    private Properties properties;

    public LoanAllocationServiceConfig(String propertiesFile) throws IOException {
        properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
        properties.load(inputStream);
    }

    private String getRecordProperty(String recordName) {
        return properties.getProperty(String.format("%s.csvFilePath", recordName));
    }

    public String bankDataSource() {
        return getRecordProperty("banks");
    }

    public String facilityDataSource() {
        return getRecordProperty("facilities");
    }

    public String covenantDataSource() {
        return getRecordProperty("covenants");
    }

    public String loanDataSource() {
        return getRecordProperty("loans");
    }

    public String assignmentDataSource() {
        return getRecordProperty("assignments");
    }

    public String yieldDataSource() {
        return getRecordProperty("yields");
    }
}
