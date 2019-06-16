package com.affirm.loans.datasource;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CsvDataSource {
    private final static char DEFAULT_DEMINATOR = ',';

    public static <T> List<T> readRecords(String csvFilePath, char deliminator, Class<T> tClass) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader()
                                        .withColumnSeparator(deliminator);
        ObjectMapper mapper = new CsvMapper();
        MappingIterator<T> it = mapper.readerFor(tClass).with(bootstrapSchema)
                    .readValues(new File(csvFilePath));
        return it.readAll();
    }

    public static <T> List<T> readRecords(String csvFilePath, Class<T> clazz) throws IOException {
        return readRecords(csvFilePath, DEFAULT_DEMINATOR, clazz);
    }

    public static <T> void writeRecords(String csvFilePath, char deliminator, Class<T> tClass, List<T> records) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(tClass).withHeader().withColumnSeparator(deliminator);

        ObjectWriter writer = mapper.writer(schema);
        writer.writeValue(new File(csvFilePath), records);
    }

    public static <T> void writeRecords(String csvFilePath, Class<T> tClass, List<T> records) throws IOException {
        writeRecords(csvFilePath, DEFAULT_DEMINATOR, tClass, records);
    }
}
