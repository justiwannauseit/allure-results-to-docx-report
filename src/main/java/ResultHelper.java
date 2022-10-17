import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log
public class ResultHelper {

    @SneakyThrows
    public static List<TestResult> getTestResults(Path allureResultPath) {
        var files = getJsonResultList(allureResultPath);
        final List<TestResult> results = new ArrayList<>();
        files.forEach(path -> {
            try {
                results.add(new ObjectMapper().readValue(path.toFile(), TestResult.class));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
        return results;
    }

    @SneakyThrows
    private static List<Path> getJsonResultList(Path reportPath) {
        final List<Path> files = Files.walk(reportPath)
                .filter(s -> s.toString().endsWith("-result.json"))
                .collect(Collectors.toList());
        log.info("Сколько нашлось результатов: " + files.size());
        return files;
    }
}
