## Рекомендации для сборки и запуска проекта:
### В файле **application.properties** задать параметры подключения к базе данных Postgresql:
1. spring.datasource.url=jdbc:postgresql://**host:port/data_base_name**
2. spring.datasource.username=**username**
3. spring.datasource.password=**password**

---
### Сборка:
>mvn clean package
---
### Запустить и тестирование:
___
>java -jar test.jar <параметр> <input.json> <output.json>

параметр - search / stat \
input.json - можно скачать по адресу /src/main/resources/test
___
