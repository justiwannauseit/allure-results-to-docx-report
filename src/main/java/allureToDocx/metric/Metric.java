package allureToDocx.metric;

import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import lombok.Data;
import lombok.extern.java.Log;

import java.util.List;

@Log
@Data
public class Metric {

    private long countAllTests;
    private long countPassedTests;
    private long countFailedTests;
    private long countBrokenTests;
    private long countSkippedTests;
    private long countUnknownTests;

    public Metric(List<TestResult> results) {
        this.countAllTests = results.size();
        this.countPassedTests = results.stream().filter(x -> x.getStatus().equals(Status.PASSED)).count();
        this.countFailedTests = results.stream().filter(x -> x.getStatus().equals(Status.FAILED)).count();
        this.countBrokenTests = results.stream().filter(x -> x.getStatus().equals(Status.BROKEN)).count();
        this.countSkippedTests = results.stream().filter(x -> x.getStatus().equals(Status.SKIPPED)).count();
        this.countUnknownTests = countAllTests - (countPassedTests + countFailedTests + countBrokenTests + countSkippedTests);

        log.info("Количество всех тестов: " + countAllTests + "");
        log.info("Количество успешных тестов: " + countPassedTests + "");
        log.info("Количество упавших тестов: " + countFailedTests + "");
        log.info("Количество поломанных тестов: " + countBrokenTests + "");
        log.info("Количество пропущенных тестов: " + countSkippedTests + "");
        log.info("Количество неизвестных тестов: " + countUnknownTests + "");
    }

}
