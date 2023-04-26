package allure.to.docx;

import allure.to.docx.core.DocxCreator;
import allure.to.docx.core.ResultParser;
import allure.to.docx.options.Options;
import allure.to.docx.util.LocaleUtil;
import com.google.devtools.common.options.OptionsParser;
import lombok.extern.java.Log;

import java.io.File;

@Log
public class Runner {

    public static void main(final String[] args) {
        OptionsParser parser = OptionsParser.newOptionsParser(Options.class);
        parser.parseAndExitUponError(args);
        var options = parser.getOptions(Options.class);

        LocaleUtil.initialize(options.language, options.country);

        var results = ResultParser.getTestResults(new File(options.from).toPath());
        DocxCreator.saveDocument(DocxCreator.createDocxFile(results), options.output);
    }

}
