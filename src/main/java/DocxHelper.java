import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class DocxHelper {

    @SneakyThrows
    public XWPFDocument createDocxFile(Map<String, String> res) {
        XWPFDocument document = new XWPFDocument();
        var temp = new XWPFDocument(new FileInputStream("./temp.docx"));

        // Создание заголовка
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);


        System.out.println(title.getStyle());

        // Фрматирование заголовка
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Отчет по тестированию");
        titleRun.setColor("000000");
        titleRun.setBold(true);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setFontSize(32);

//        document.createStyles();
//        document.createStyles().addStyle(temp.getStyles().getStyle("Heading1"));
//        document.createStyles().addStyle(temp.getStyles().getStyle("Heading2"));
//        titleRun.setStyle("Heading1");


        // Подзаголовок
        XWPFParagraph subTitle = document.createParagraph();
        subTitle.setAlignment(ParagraphAlignment.CENTER);
        // Фотрматирование подзаголовка
        XWPFRun subTitleRun = subTitle.createRun();
        subTitleRun.setText("Результаты тестирования за 10.10.2022");
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
                Units.toEMU(450), Units.toEMU(450));


        //Создадим таблицу
//        var map = Map.of("Проверка работы страницы авторизации", "PASSED", "Авторизация в системе под ролью Иван", "FAILED");
        generateTable2X2FromMap(document, res);

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

    private static void generateTable2X2FromMap(XWPFDocument document, Map<String, String> map) {
        XWPFTable table = document.createTable(map.size() + 1, 2);

        //Получим список заголовки
        var keys = map.keySet().stream().collect(Collectors.toList());
        var values = map.values().stream().collect(Collectors.toList());

        createParagraphForTableTitle(table.getRow(0).getCell(0).getParagraphs().get(0), "Тест кейс");
        createParagraphForTableTitle(table.getRow(0).getCell(1).getParagraphs().get(0), "Статус");

        //Добавим Заголоки тест кейсов
        for (int i = 0; i < map.size(); i++) {
            createParagraphForTableBodyTestCaseName(table.getRow(i + 1).getCell(0).getParagraphs().get(0), keys.get(i));
        }

        //Добавим Статусы тест кейсов
        for (int i = 0; i < map.size(); i++) {
            if (values.get(i).contains("FAILED")) {
                table.getRow(i + 1).getCell(1).setColor("facc9d");
            }
            createParagraphForTableBodyStatus(table.getRow(i + 1).getCell(1).getParagraphs().get(0), values.get(i));
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

}
