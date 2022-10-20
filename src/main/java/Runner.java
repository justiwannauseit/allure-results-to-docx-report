import lombok.extern.java.Log;

import java.io.File;

@Log
public class Runner {

    public static void main(String[] args) {

        var results = ResultHelper.getTestResults(new File("./allure-results").toPath());

        /**Если два теста имеют одинаковые названия то в мапу положется только один какой-то...
         * лучше брать лист результатов и исходя из этого делать табличку*/
        //Мапа в которой лежит статус и назавние теста
//        var map = new HashMap<String, String>();
//        results.forEach(x -> map.put(x.getFullName(), x.getStatus().toString()));

        //Печатаем отчет
        DocxHelper.saveDocument(DocxHelper.createDocxFile(results), "out.docx");

    }

}
