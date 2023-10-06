package handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.ErrorOutput;
import model.search.Result;
import model.statistic.model.StatisticsResult;

import java.io.File;
import java.io.IOException;

public class OutputHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void writeSearchResult(String jsonFilename, Result result) throws IOException {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.writeValue(new File(jsonFilename), result);
    }

    public void writeStatisticsOutput(String jsonFilename, StatisticsResult result) throws IOException {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.writeValue(new File(jsonFilename), result);
    }

    public void writeErrorOutput(String jsonFilename, String errorMessage) throws IOException {
        ErrorOutput errorOutput = new ErrorOutput();
        errorOutput.setType("error");
        errorOutput.setMessage(errorMessage);
        objectMapper.writeValue(new File(jsonFilename), errorOutput);
    }
}
