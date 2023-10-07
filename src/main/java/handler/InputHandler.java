package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import model.criteria.Criteria;
import model.statistic.StatisticsInput;

import java.io.IOException;

public class InputHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Criteria parseSearchCriteria(String json) throws IOException {
        return objectMapper.readValue(json, Criteria.class);
    }

    public StatisticsInput parseStatisticsInput(String json) throws IOException {
        return objectMapper.readValue(json, StatisticsInput.class);
    }
}
