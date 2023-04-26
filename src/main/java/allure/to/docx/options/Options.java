package allure.to.docx.options;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

public class Options extends OptionsBase {

    @Option(
            name = "from",
            abbrev = 'f',
            help = "Path to allure result directory",
            category = "startup",
            defaultValue = "./allure-results"
    )
    public String from;

    @Option(
            name = "out",
            abbrev = 'o',
            help = "Path to the generated report file",
            category = "startup",
            defaultValue = "out.docx"
    )
    public String output;

    @Option(
            name = "lang",
            abbrev = 'l',
            help = "Lang for report",
            category = "startup",
            defaultValue = "ru"
    )
    public String language;

    @Option(
            name = "country",
            abbrev = 'c',
            help = "Country for report",
            category = "startup",
            defaultValue = "RU"
    )
    public String country;

}
