## Рекомендации для сборки и запуска проекта:
### В файле **application.properties** задать параметры подключения к базе данных Postgresql:
1. spring.datasource.url=jdbc:postgresql://**host:port/data_base_name**
2. spring.datasource.username=**username**
3. spring.datasource.password=**password**

---
### Сборка:
>mvn clean package
---
### Запуск и тестирование:
___
>java -jar test.jar <параметр> <input.json> <output.json>

параметр - search / stat \
input.json - можно найти по адресу /src/main/resources/test
___
