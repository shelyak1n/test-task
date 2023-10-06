import handler.InputHandler;
import handler.OutputHandler;
import model.criteria.Criteria;
import model.statistic.model.StatisticsInput;
import util.SearchCriteria;
import util.SearchResult;
import util.StatisticsOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java -jar program.jar operation input.json output.json");
            return;
        }

        String operation = args[0];
        String inputJsonFile = args[1];
        String outputJsonFile = args[2];

        try {
            InputHandler inputHandler = new InputHandler();
            OutputHandler outputHandler = new OutputHandler();

            if ("search".equalsIgnoreCase(operation)) {
                String jsonInput = new String(Files.readAllBytes(Paths.get(inputJsonFile)));
                Criteria criteria = inputHandler.parseSearchCriteria(jsonInput);
                SearchCriteria searchCriteria = new SearchCriteria(criteria);
                SearchResult searchResult = new SearchResult(searchCriteria.arrayListCriteria());
                outputHandler.writeSearchResult(outputJsonFile, searchResult.getResult());
            } else if ("stat".equalsIgnoreCase(operation)) {
                String jsonInput = new String(Files.readAllBytes(Paths.get(inputJsonFile)));
                StatisticsInput statisticsInput = inputHandler.parseStatisticsInput(jsonInput);
                StatisticsOutput statisticsOutput = new StatisticsOutput(statisticsInput);
                outputHandler.writeStatisticsOutput(outputJsonFile, statisticsOutput.getStatisticsResult());
            } else {
                outputHandler.writeErrorOutput(outputJsonFile, "Invalid operation");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
