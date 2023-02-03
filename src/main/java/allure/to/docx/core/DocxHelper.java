package allure.to.docx.core;

import allure.to.docx.graphics.PieChartGenerator;
import allure.to.docx.metric.Metric;
import allure.to.docx.style.Colors;
import allure.to.docx.util.LocaleUtil;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static allure.to.docx.util.LocaleUtil.REPORT_CONFIGURATION;
import static allure.to.docx.util.LocaleUtil.localizeCode;

@UtilityClass
public class DocxHelper {

    @SneakyThrows
    public XWPFDocument createDocxFile(final List<TestResult> results) {

        //Получим метрики
        var metric = new Metric(results);

        //Сохраним круговую диаграмму результатов
        PieChartGenerator.save(metric);

        XWPFDocument document = new XWPFDocument();

        // Создание заголовка
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);

        // Форматирование заголовка
        XWPFRun titleRun = title.createRun();
        titleRun.setText(LocaleUtil.localizeCode("test.report"));
        titleRun.setColor(REPORT_CONFIGURATION.titleColor());
        titleRun.setBold(true);
        titleRun.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        titleRun.setFontSize(REPORT_CONFIGURATION.fontSize());

        // Подзаголовок
        XWPFParagraph subTitle = document.createParagraph();
        subTitle.setAlignment(ParagraphAlignment.CENTER);

        // Форматирование подзаголовка
        XWPFRun subTitleRun = subTitle.createRun();
        var date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(REPORT_CONFIGURATION.datePattern());

        subTitleRun.setText(LocaleUtil.localizeCode("test.period") + date.format(formatter));
        subTitleRun.setColor(REPORT_CONFIGURATION.titleColor());
        subTitleRun.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        subTitleRun.setFontSize(REPORT_CONFIGURATION.subTitleFontSize());
        subTitleRun.setTextPosition(REPORT_CONFIGURATION.textPosition());
        subTitleRun.setUnderline(UnderlinePatterns.SINGLE);

        // Вставка изображения
        XWPFParagraph image = document.createParagraph();
        image.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun imageRun = image.createRun();
        imageRun.setTextPosition(REPORT_CONFIGURATION.subTitleFontSize());

//        Path imagePath = Paths.get(ClassLoader.getSystemResource("./output_image.png").toURI());
        var imagePath = new File(REPORT_CONFIGURATION.outputPath()).toPath();
        imageRun.addPicture(Files.newInputStream(imagePath),
                XWPFDocument.PICTURE_TYPE_PNG, imagePath.getFileName().toString(),
                Units.toEMU(350), Units.toEMU(350));

        printMetrics(document, metric);

        //Создадим таблицу
        generateTable2X2FromMap(document, results);


        // Напишем, что дальше пойдут результаты:
        // Создание заголовка
        XWPFParagraph title2 = document.createParagraph();
        title2.setAlignment(ParagraphAlignment.LEFT);

        // Форматирование заголовка
        XWPFRun titleRun2 = title2.createRun();
        titleRun2.setText(LocaleUtil.localizeCode("test.cases"));
        titleRun2.setColor(REPORT_CONFIGURATION.titleColor());
        titleRun2.setBold(true);
        titleRun2.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        titleRun2.setFontSize(REPORT_CONFIGURATION.fontSize());

        //Печатаем сами шаги
        results.forEach(result -> printSteps(document, result));

        return document;
    }

    @SneakyThrows
    public static void saveDocument(XWPFDocument document, String fileName) {
        FileOutputStream out = new FileOutputStream(fileName);
        document.write(out);
        out.close();
        document.close();
    }

    private static void printMetrics(XWPFDocument document, Metric metric) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        printCurrentMetric(paragraph, LocaleUtil.localizeCode("tests.total") + metric.getCountAllTests() + "; ");
        printCurrentMetric(paragraph, LocaleUtil.localizeCode("tests.successful") + metric.getCountPassedTests() + "; ");
        printCurrentMetric(paragraph, LocaleUtil.localizeCode("tests.failed") + metric.getCountFailedTests() + "; ");
        printCurrentMetric(paragraph, LocaleUtil.localizeCode("tests.broken") + metric.getCountBrokenTests() + "; ");
        printCurrentMetric(paragraph, LocaleUtil.localizeCode("tests.skipped") + metric.getCountSkippedTests() + "; ");
        printCurrentMetric(paragraph, LocaleUtil.localizeCode("tests.unknown") + metric.getCountUnknownTests() + ".");
    }

    private static void printCurrentMetric(XWPFParagraph paragraph, String text) {
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText(text);
        paragraphRun.setColor(REPORT_CONFIGURATION.titleColor());
        paragraphRun.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        paragraphRun.setFontSize(REPORT_CONFIGURATION.metricFontSize());
        paragraphRun.setTextPosition(REPORT_CONFIGURATION.textPosition());
        paragraphRun.setBold(true);
    }

    private static void generateTable2X2FromMap(XWPFDocument document, List<TestResult> results) {
        XWPFTable table = document.createTable(results.size() + 1, 2);

        createParagraphForTableTitle(table.getRow(0).getCell(0).getParagraphs().get(0), localizeCode("test.case"));
        createParagraphForTableTitle(table.getRow(0).getCell(1).getParagraphs().get(0), localizeCode("test.status"));

        //Добавим Заголоки тест кейсов
        for (int i = 0; i < results.size(); i++) {
            createParagraphForTableBodyTestCaseName(table.getRow(i + 1).getCell(0).getParagraphs().get(0), results.get(i).getFullName());

            var status = results.get(i).getStatus();
            if (status.equals(Status.FAILED)) {
                table.getRow(i + 1).getCell(1).setColor(REPORT_CONFIGURATION.failedColor());
            }
            createParagraphForTableBodyStatus(table.getRow(i + 1).getCell(1).getParagraphs().get(0), status.value());
        }
    }

    private void createParagraphForTableTitle(XWPFParagraph paragraph, String text) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText(text);
        paragraphRun.setColor(REPORT_CONFIGURATION.titleColor());
        paragraphRun.setBold(true);
        paragraphRun.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        paragraphRun.setFontSize(REPORT_CONFIGURATION.metricFontSize());

    }

    private void createParagraphForTableBodyTestCaseName(XWPFParagraph paragraph, String text) {
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText(text);
        paragraphRun.setColor(REPORT_CONFIGURATION.titleColor());
        paragraphRun.setBold(false);
        paragraphRun.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        paragraphRun.setFontSize(REPORT_CONFIGURATION.metricFontSize());

        paragraphRun.addBreak();
    }

    private void createParagraphForTableBodyStatus(XWPFParagraph paragraph, String text) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText(text);
        paragraphRun.setColor(REPORT_CONFIGURATION.titleColor());
        paragraphRun.setBold(false);
        paragraphRun.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        paragraphRun.setFontSize(REPORT_CONFIGURATION.metricFontSize());
    }

    private void printSteps(XWPFDocument document, TestResult result) {
        //Это мы только заголовок напечатали и статус

        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.LEFT);
        // Форматирование подзаголовка
        XWPFRun subTitleRun = title.createRun();
        subTitleRun.setText("\n" + result.getFullName());
        subTitleRun.setColor(REPORT_CONFIGURATION.titleColor());
        subTitleRun.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        subTitleRun.setFontSize(REPORT_CONFIGURATION.stepsFontSize());
        subTitleRun.setTextPosition(REPORT_CONFIGURATION.stepsTextPosition());
        subTitleRun.setBold(true);

        var statusColor = title.createRun();
        setColorByStatus(result.getStatus(), statusColor);
        statusColor.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        statusColor.setFontSize(REPORT_CONFIGURATION.stepsFontSize());
        statusColor.setTextPosition(REPORT_CONFIGURATION.stepsTextPosition());
        statusColor.setBold(true);
        statusColor.setText(REPORT_CONFIGURATION.statusSeparator());

        var status = title.createRun();
        status.setColor(REPORT_CONFIGURATION.titleColor());
        status.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        status.setFontSize(REPORT_CONFIGURATION.stepsFontSize());
        status.setTextPosition(REPORT_CONFIGURATION.stepsTextPosition());
        status.setBold(true);
        status.setText(result.getStatus().name());

        //А теперь уже напечатаем сами шаги:
        result.getSteps().forEach(x -> printRealSteps(document, x));
    }

    private void printRealSteps(XWPFDocument document, StepResult stepResult) {
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun subTitleRun = title.createRun();
        setColorByStatus(stepResult.getStatus(), subTitleRun);
        subTitleRun.setText("[" + stepResult.getStatus() + "] " + stepResult.getName());
        subTitleRun.setFontFamily(REPORT_CONFIGURATION.fontFamily());
        subTitleRun.setFontSize(REPORT_CONFIGURATION.metricFontSize());
        subTitleRun.setTextPosition(REPORT_CONFIGURATION.textPosition());

    }

    private void setColorByStatus(Status status, XWPFRun subTitleRun) {
        if (status.equals(Status.FAILED)) {
            subTitleRun.setColor(Colors.FAILED.getHexCode());
        }
        if (status.equals(Status.SKIPPED)) {
            subTitleRun.setColor(Colors.SKIPPED.getHexCode());
        }
        if (status.equals(Status.PASSED)) {
            subTitleRun.setColor(Colors.PASSED.getHexCode());
        }
        if (status.equals(Status.BROKEN)) {
            subTitleRun.setColor(Colors.BROKEN.getHexCode());
        }
    }

}
