package allureToDocx.core;

import allureToDocx.graphics.PieChartGenerator;
import allureToDocx.metric.Metric;
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

@UtilityClass
public class DocxHelper {

    @SneakyThrows
    public XWPFDocument createDocxFile(final List<TestResult> results) {

        //Получим метрики
        var metric = new Metric(results);
        //Сохраним круговую диограмму результатов
        PieChartGenerator.save(metric);

        XWPFDocument document = new XWPFDocument();

        // Создание заголовка
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);

        // Фрматирование заголовка
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Отчет по тестированию");
        titleRun.setColor("000000");
        titleRun.setBold(true);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setFontSize(32);

        // Подзаголовок
        XWPFParagraph subTitle = document.createParagraph();
        subTitle.setAlignment(ParagraphAlignment.CENTER);
        // Фотрматирование подзаголовка
        XWPFRun subTitleRun = subTitle.createRun();
        var date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        subTitleRun.setText("Результаты тестирования за " + date.format(formatter));
        subTitleRun.setColor("000000");
        subTitleRun.setFontFamily("Times New Roman");
        subTitleRun.setFontSize(16);
        subTitleRun.setTextPosition(20);
        subTitleRun.setUnderline(UnderlinePatterns.SINGLE);

        // Вставка изображения
        XWPFParagraph image = document.createParagraph();
        image.setAlignment(ParagraphAlignment.CENTER);
        //
        XWPFRun imageRun = image.createRun();
        imageRun.setTextPosition(20);

//        Path imagePath = Paths.get(ClassLoader.getSystemResource("./output_image.png").toURI());
        var imagePath = new File("./output_image.png").toPath();
        imageRun.addPicture(Files.newInputStream(imagePath),
                XWPFDocument.PICTURE_TYPE_PNG, imagePath.getFileName().toString(),
                Units.toEMU(350), Units.toEMU(350));

        printMetrics(document, metric);

        //Создадим таблицу
        generateTable2X2FromMap(document, results);

        /**
         * Напишем, что дальше пойдут результаты:
         * */
        // Создание заголовка
        XWPFParagraph title2 = document.createParagraph();
        title2.setAlignment(ParagraphAlignment.LEFT);
        // Фрматирование заголовка
        XWPFRun titleRun2 = title2.createRun();
        titleRun2.setText("Тест кейсы");
        titleRun2.setColor("000000");
        titleRun2.setBold(true);
        titleRun2.setFontFamily("Times New Roman");
        titleRun2.setFontSize(32);

        //Печатаем сами шаги
        results.forEach(result -> printSteps(document, result));

        return document;
    }

    @SneakyThrows
    public static boolean saveDocument(XWPFDocument document, String fileName) {
        FileOutputStream out = new FileOutputStream(fileName);
        document.write(out);
        out.close();
        document.close();
        return true;
    }

    private static void printMetrics(XWPFDocument document, Metric metric) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        printCurrentMetric(paragraph, "Всего: " + metric.getCountAllTests() + "; ");
        printCurrentMetric(paragraph, "Успешных: " + metric.getCountPassedTests() + "; ");
        printCurrentMetric(paragraph, "Проваленных: " + metric.getCountFailedTests() + "; ");
        printCurrentMetric(paragraph, "Сломанных: " + metric.getCountBrokenTests() + "; ");
        printCurrentMetric(paragraph, "Пропущенных: " + metric.getCountSkippedTests() + "; ");
        printCurrentMetric(paragraph, "Неизвестных: " + metric.getCountUnknownTests() + ".");
    }

    private static void printCurrentMetric(XWPFParagraph paragraph, String text) {
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText(text);
        paragraphRun.setColor("000000");
        paragraphRun.setFontFamily("Times New Roman");
        paragraphRun.setFontSize(12);
        paragraphRun.setTextPosition(20);
        paragraphRun.setBold(true);
    }

    private static void generateTable2X2FromMap(XWPFDocument document, List<TestResult> results) {
        XWPFTable table = document.createTable(results.size() + 1, 2);

        createParagraphForTableTitle(table.getRow(0).getCell(0).getParagraphs().get(0), "Тест кейс");
        createParagraphForTableTitle(table.getRow(0).getCell(1).getParagraphs().get(0), "Статус");

        //Добавим Заголоки тест кейсов
        for (int i = 0; i < results.size(); i++) {
            createParagraphForTableBodyTestCaseName(table.getRow(i + 1).getCell(0).getParagraphs().get(0), results.get(i).getFullName());

            var status = results.get(i).getStatus();
            if (status.equals(Status.FAILED)) {
                table.getRow(i + 1).getCell(1).setColor("facc9d");
            }
            createParagraphForTableBodyStatus(table.getRow(i + 1).getCell(1).getParagraphs().get(0), status.value());
        }
    }

    private XWPFParagraph createParagraphForTableTitle(XWPFParagraph paragraph, String text) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText(text);
        paragraphRun.setColor("000000");
        paragraphRun.setBold(true);
        paragraphRun.setFontFamily("Times New Roman");
        paragraphRun.setFontSize(12);

        return paragraph;
    }

    private XWPFParagraph createParagraphForTableBodyTestCaseName(XWPFParagraph paragraph, String text) {
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText(text);
        paragraphRun.setColor("000000");
        paragraphRun.setBold(false);
        paragraphRun.setFontFamily("Times New Roman");
        paragraphRun.setFontSize(12);

        paragraphRun.addBreak();

        return paragraph;
    }

    private XWPFParagraph createParagraphForTableBodyStatus(XWPFParagraph paragraph, String text) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText(text);
        paragraphRun.setColor("000000");
        paragraphRun.setBold(false);
        paragraphRun.setFontFamily("Times New Roman");
        paragraphRun.setFontSize(12);

        return paragraph;
    }

    private void printSteps(XWPFDocument document, TestResult result) {
        //Это мы только заголовок напечатали и статус

        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.LEFT);
        // Фотрматирование подзаголовка
        XWPFRun subTitleRun = title.createRun();
        subTitleRun.setText("\n" + result.getFullName());
        subTitleRun.setColor("000000");
        subTitleRun.setFontFamily("Times New Roman");
        subTitleRun.setFontSize(14);
        subTitleRun.setTextPosition(0);
        subTitleRun.setBold(true);

        var statusColor = title.createRun();
        setColorByStatus(result.getStatus(), statusColor);
        statusColor.setFontFamily("Times New Roman");
        statusColor.setFontSize(14);
        statusColor.setTextPosition(0);
        statusColor.setBold(true);
        statusColor.setText(" ► ");

        var status = title.createRun();
        status.setColor("000000");
        status.setFontFamily("Times New Roman");
        status.setFontSize(14);
        status.setTextPosition(0);
        status.setBold(true);
        status.setText(result.getStatus().name());

        //А теперь уже напечатем сами шаги:
        result.getSteps().forEach(x -> printRealSteps(document, x));
    }

    private void printRealSteps(XWPFDocument document, StepResult stepResult) {
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun subTitleRun = title.createRun();
        setColorByStatus(stepResult.getStatus(), subTitleRun);
        subTitleRun.setText("[" + stepResult.getStatus() + "] " + stepResult.getName());
        subTitleRun.setFontFamily("Times New Roman");
        subTitleRun.setFontSize(12);
        subTitleRun.setTextPosition(20);

    }

    private void setColorByStatus(Status status, XWPFRun subTitleRun) {
        if (status.equals(Status.FAILED)) {
            subTitleRun.setColor("FF0000");
        }
        if (status.equals(Status.SKIPPED)) {
            subTitleRun.setColor("6e6e6e");
        }
        if (status.equals(Status.PASSED)) {
            subTitleRun.setColor("000000");
        }
        if (status.equals(Status.BROKEN)) {
            subTitleRun.setColor("d0d40b");
        }
    }

}
