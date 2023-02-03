# Report generator from Allure Results to Docx

#### _____________

## ***Now supports Russian and English languages***

## Описание

Генерация проходит из allure-results

1. В папке должны лежать json файлы.
2. Логирование не попадает в отчет

## Запуск

### Запуск через jar файл

```
java -jar docAllure.jar -Dfrom="./allure-result" -Dout="out.docx"
```

### Запуск в проекте

```
В классе Runner прописать путь до папки с отчетом и имя файла docx
```

## Description

Result converter from allure-result folder in your project

1. The folder should contain json files with results
2. Logging is not noted in report

## Run guide

### Run via jar file

```
java -jar docAllure.jar -Dfrom="./allure-result" -Dout="out.docx"
```

### Run via project Runner class

```
in a Runner class write path to folder with reports and name of output file (.docx) 
```