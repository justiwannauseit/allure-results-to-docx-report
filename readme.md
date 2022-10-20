# Report generator from Allure Results to Docx

#### _____________

## ***ALPHA VERSION only for Russian Language***

## Описание

Генерация проходит из allure-results

1. В папке должны лежать json файлы.
2. Логирование не попадает в отчет

## Запуск

### Запуск через jar файл

```
java -jar allure-to-docx.jar -Dfrom="./allure-result" -Dout="out.docx"
```

### Запуск в проекте

```
В классе Runner прописать путь до папки с отчетом и имя файла docx
```


