package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.ErrorOutput;
import model.Result;

import java.io.File;
import java.io.IOException;

public class OutputHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void writeSearchResult(String jsonFilename, Result result) throws IOException {
        objectMapper.writeValue(new File(jsonFilename), result);
    }

//    public void writeStatisticsOutput(String jsonFilename, StatisticsOutput result) throws IOException {
//        objectMapper.writeValue(new File(jsonFilename), result);
//    }

    public void writeErrorOutput(String jsonFilename, String errorMessage) throws IOException {
        ErrorOutput errorOutput = new ErrorOutput();
        errorOutput.setType("error");
        errorOutput.setMessage(errorMessage);
        objectMapper.writeValue(new File(jsonFilename), errorOutput);
    }
}
