package allure.to.docx.util;

import allure.to.docx.config.ReportConfiguration;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigFactory;

import java.util.Locale;
import java.util.ResourceBundle;

@UtilityClass
public class LocaleUtil {

    public static final ReportConfiguration REPORT_CONFIGURATION = ConfigFactory.create(ReportConfiguration.class);
    private static final Locale currentLocale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
    public static final ResourceBundle bundle = ResourceBundle.getBundle("l10n", currentLocale);

    public static String localizeCode(String code) {
        if (bundle.containsKey(code)) {
            return bundle.getString(code);
        } else {
            return code;
        }
    }

}
