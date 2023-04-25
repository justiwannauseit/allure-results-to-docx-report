# Report generator from Allure Results to Docx

#### _____________

## ***Now supports Russian and English languages***
### ***You can build your version jar via gradle task 'jar'***
## Описание

Генерация проходит из allure-results

1. В папке должны лежать json файлы.
2. Логирование не попадает в отчет

## Запуск

### Запуск через jar файл

```
java -jar docAllure.jar --from='./allure-result' --out='out.docx'
```

### Список опций

1) `--from` - путь до папки Allure result. Программа должна понять откуда брать json файлы. По умолчанию указана текущая
   папка: `./allure-results`

2) `--out` - параметр указывает куда сохранить файл. Программа должна понять куда сохранять. Важно указать
   расширение `docx`. По умолчанию указано: `./out.docx`

3) `--lang` - язык для локализации. По умолчанию указано: `ru`

4) `--country` - страна для локализации. По умолчанию указано: `RU`

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
java -jar docAllure.jar --from='./allure-result' --out='out.docx'
```

### Options list

1) `--from` - path to the Allure result folder. The program must know where to get json files from. By default, the
   current folder is specified: `./allure-results`

2) `--out` - the parameter specifies where to save the file. The program must understand where to save. It is important
   to specify the `docx` extension. Default: `./out.docx`

3) `--lang` - language for localization. Default: `ru`

4) `--country` - country for localization. Default: `RU`

### Run via project Runner class

```
in a Runner class write path to folder with reports and name of output file (.docx) 
```