import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import handler.InputHandler;
import handler.OutputHandler;
import handler.SearchHandler;
import handler.StatisticsHandler;
import model.ErrorOutput;
import model.criteria.Criteria;
import model.search.Result;
import model.statistic.StatisticsInput;
import model.statistic.StatisticsResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java -jar program.jar operation input.json output.json");
            return;
        }

        String operation = args[0];
        String inputJsonFile = args[1];
        String outputJsonFile = args[2];

        InputHandler inputHandler = new InputHandler();
        OutputHandler outputHandler = new OutputHandler();

        if ("search".equalsIgnoreCase(operation)) {
            try {
                String jsonInput = new String(Files.readAllBytes(Paths.get(inputJsonFile)));
                Criteria criteria = inputHandler.parseSearchCriteria(jsonInput);
                SearchHandler searchHandler = new SearchHandler();
                Result searchResult = searchHandler.performSearch(criteria);
                outputHandler.writeSearchResult(outputJsonFile, searchResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("stat".equalsIgnoreCase(operation)) {
            try {
                String jsonInput = new String(Files.readAllBytes(Paths.get(inputJsonFile)));
                StatisticsInput statisticsInput = inputHandler.parseStatisticsInput(jsonInput);
                StatisticsHandler statisticsHandler = new StatisticsHandler();
                StatisticsResult statisticsResult = statisticsHandler.performStatistics(statisticsInput);
                outputHandler.writeStatisticsOutput(outputJsonFile, statisticsResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            outputHandler.writeErrorOutput(outputJsonFile, "Invalid operation");
        }
    }
}
