import handler.InputHandler;
import handler.OutputHandler;
import model.Criteria;
import util.SearchCriteria;
import util.SearchResult;

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
                // получаем json из input.json
                String jsonInput = new String(Files.readAllBytes(Paths.get(inputJsonFile)));
                // json -> pojo
                Criteria criteria = inputHandler.parseSearchCriteria(jsonInput);
                // создание списка критериев
                SearchCriteria searchCriteria = new SearchCriteria(criteria);
                // Выполните операцию поиска с использованием criteria
                SearchResult searchResult = new SearchResult(searchCriteria.arrayListCriteria());
                // Запишите результат в выходной файл
                outputHandler.writeSearchResult(outputJsonFile, searchResult.getResult());
//            } else if ("stat".equalsIgnoreCase(operation)) {
//                // получаем json из input.json
//                String jsonInput = new String(Files.readAllBytes(Paths.get(inputJsonFile)));
//                //json -> pojo
//                StatisticsInput statisticsInput = inputHandler.parseStatisticsInput(jsonInput);
//                // Выполните операцию статистики с использованием statisticsInput
//                StatisticsOutput statisticsOutput = new StatisticsOutput();
//                // Запишите результат в выходной файл
//                outputHandler.writeStatisticsOutput(outputJsonFile, statisticsOutput);
            } else {
                outputHandler.writeErrorOutput(outputJsonFile, "Invalid operation");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
