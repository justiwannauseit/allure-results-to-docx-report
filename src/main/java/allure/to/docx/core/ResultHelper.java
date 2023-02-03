package allure.to.docx.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
@UtilityClass
public class ResultHelper {

    @SneakyThrows
    public static List<TestResult> getTestResults(final Path allureResultPath) {
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
    private static List<Path> getJsonResultList(final Path reportPath) {
        final List<Path> files;

        try (Stream<Path> walk = Files.walk(reportPath)) {
            files = walk.filter(s -> s.toString().endsWith("-result.json"))
                    .collect(Collectors.toList());
        }

        log.info("Сколько нашлось результатов: " + files.size());

        return files;
    }
}
