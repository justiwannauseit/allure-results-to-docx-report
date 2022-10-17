import io.qameta.allure.model.Status;
import lombok.extern.java.Log;

import java.io.File;
import java.util.HashMap;

@Log
public class Runner {

    public static void main(String[] args) {

        var results = ResultHelper.getTestResults(new File("allure-results").toPath());

        //Посчитать сколько всего тестов и сколько успешных тестов
        long countAllTests = results.size();

        long countPassedTests = results.stream().filter(x -> x.getStatus().equals(Status.PASSED)).count();
        long countFailedTests = results.stream().filter(x -> x.getStatus().equals(Status.FAILED)).count();
        long countBrokenTests = results.stream().filter(x -> x.getStatus().equals(Status.BROKEN)).count();
        long countSkippedTests = results.stream().filter(x -> x.getStatus().equals(Status.SKIPPED)).count();
        long countUnknownTests = results.stream().filter(x -> x.getStatus().value().contains("now")).count();

        /**TODO УДАЛИТЬ!!!*/
        countPassedTests = 20;

        /**Если два теста имеют одинаковые названия то в мапу положется только один какой-то...
         * лучше брать лист результатов и исходя из этого делать табличку*/

        log.info(countAllTests + "");
        log.info(countPassedTests + "");
        log.info(countFailedTests + "");
        log.info(countBrokenTests + "");
        log.info(countSkippedTests + "");
        log.info(countUnknownTests + "");

        //Мапа в которой лежит статус и назавние теста
        var map = new HashMap<String, String>();
        results.forEach(x -> map.put(x.getFullName(), x.getStatus().toString()));


        PieChartGenerator.save(countPassedTests,
                countFailedTests,
                countBrokenTests,
                countSkippedTests,
                countUnknownTests);
        DocxHelper.saveDocument(DocxHelper.createDocxFile(map), "out.docx");

    }

}
