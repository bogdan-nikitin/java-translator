# Лабораторная работа по Java

Веб-приложения переводчика

Взаимодействие происходит через веб-интерфейс

В качестве стороннего сервиса перевода был использован 
[LibreTranslate](https://github.com/LibreTranslate/LibreTranslate/tree/main)

## Инструкция по запуску

Примеры приведены на bash

### Вам потребуется: 

* Java 22 или новее
* PostgreSQL 16 или новее
* Maven 3.5 или новее

### Настройка PostgreSQL

Создайте базу данных. Сохраните URL, имя пользователя и пароль 
базы данных в переменных среды `DATABASE_URL`, 
`DATABASE_USERNAME` и `DATABASE_PASSWORD` 
соответственно. Например
```bash
export DATABASE_URL=postgresql://localhost:5432/translator
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=postgres
```

Таблица будет создана автоматически при запуске приложения
(для простоты запуска)

Таблица имеет следующие поля:
* `id` - идентификатор записи
* `ip` - IP пользователя, отправившего запрос
* `successful` - флаг, обработан ли запрос успешно
* `time` - время логирования
* `query` - текст запроса
* `response` - результат перевода или текст ошибки (в зависимости от значения `successful`)

### Настройка внешнего сервиса

Сохраните полный URL (с указанием схемы) внешнего сервиса в переменную среды
`TRANSLATOR_API_URL`. Например
```bash
export TRANSLATOR_API_URL=https://translate.terraprint.co/
```

Вы можете [запустить сервис локально](https://github.com/LibreTranslate/LibreTranslate/tree/main?tab=readme-ov-file#run-with-docker)
или воспользоваться [зеркалом](https://github.com/LibreTranslate/LibreTranslate/tree/main?tab=readme-ov-file#mirrors).
Для работы перевода 
**используемый сервис не должен использовать API-ключи** ([libretranslate.com](https://libretranslate.com/) **не** подойдёт)

При разработке использовался локально запущенный образ

### Запуск проекта

Склонируйте проект

```bash
git clone https://github.com/bogdan-nikitin/java-translator.git
cd java-translator
```

Запустите через `mvnw` (bash, UNIX) или `mvnw.cmd` (CMD, Windows)
```bash
./mvnw spring-boot:run
```

### Запуск тестов

Аналогично предыдущему

```bash
./mvnw test
```

## Использование

Приложение запустится на порту 8080. Для доступа к веб-интерфейсу
перейдите на страницу http://localhost:8080

В левой колонке можно выбрать язык запроса и ввести сам запрос

В правой колонке можно выбрать язык, на который требуется перевести

Для перевода нажмите кнопку "Translate"

Если произойдёт ошибка, она будет выведена под кнопкой

Запросы (успешные и завершившиеся ошибкой) логируются в консоль и базу данных