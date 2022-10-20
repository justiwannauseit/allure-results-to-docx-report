import lombok.extern.java.Log;

import java.io.File;

@Log
public class Runner {

    public static void main(final String... args) {

        var from = System.getProperty("from", "./allure-results");
        var out = System.getProperty("from", "out.docx");

        var results = ResultHelper.getTestResults(new File(from).toPath());
        DocxHelper.saveDocument(DocxHelper.createDocxFile(results), out);
    }

}
