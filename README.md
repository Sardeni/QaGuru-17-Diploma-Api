# Проект по автоматизации тестирования сервиса petstore.swagger.io

<br>
<br>

## :pushpin: Содержание:

- [Использованный стек технологий](#computer-использованный-стек-технологий)
- [Реализованные проверки](#computer-реализованные-проверки)
- [Запуск тестов](#running_woman-запуск-тестов)
- [Сборка в Jenkins](#-сборка-в-jenkins)
- [Пример Allure-отчета](#-пример-allure-отчета)
- [Интеграция с Allure TestOps](#-интеграция-с-allure-testops)
- [Уведомления в Telegram с использованием бота](#-уведомления-в-telegram-с-использованием-бота)

## :computer: Использованный стек технологий

<p align="center">
<img width="6%" title="IntelliJ IDEA" src="images/logo/Intelij_IDEA.svg">
<img width="6%" title="Java" src="images/logo/Java.svg">
<img width="6%" title="Java" src="images/logo/restAssured.png">
<img width="6%" title="Allure Report" src="images/logo/Allure_Report.svg">
<img width="5%" title="Allure TestOps" src="images/logo/allureTestOps.svg">
<img width="6%" title="Gradle" src="images/logo/Gradle.svg">
<img width="6%" title="JUnit5" src="images/logo/JUnit5.svg">
<img width="6%" title="GitHub" src="images/logo/GitHub.svg">
<img width="6%" title="Jenkins" src="images/logo/Jenkins.svg">
<img width="6%" title="Jira" src="images/logo/Jira.svg">
<img width="6%" title="Telegram" src="images/logo/Telegram.svg">
</p>

Автотесты написаны на <code>Java</code> с использованием <code>JUnit 5</code> и <code>Gradle</code>.
Для тестов использована библиотека [REST Assured](https://rest-assured.io/).
Запуск тестов можно осуществлять локально или с помощью Jenkins/Allure TestOps.
Также реализована сборка в <code>Jenkins</code> с формированием Allure-отчета и отправкой уведомления с результатами в <code>Telegram</code> после завершения прогона.

## :computer: Реализованные проверки

- [x] *POST /pet - создание новой учетной записи о животном в магазине*
- [x] *GET /pet/{petId} - получение данных о животном по Id*
- [x] *POST /pet/{petId} - обновление данных о животном (имя/статус)*
- [x] *PUT /pet - обновление данных о животном (все параметры)*
- [x] *DELETE /pet/{petId - удаление данных о животном из базы магазина}*
- [x] *Для каждого метода была сделана одна негативная проверка*

## :running_woman: Запуск тестов

### Локальный запуск тестов
```
gradle clean test
```

При необходимости можно переопределить параметры запуска
```
gradle clean 
test/smoke/regress - запуск всех тестов/только smoke/только regress
```

### Запуск тестов на удаленном браузере из Jenkins/Allure TestOps
```
gradle clean test
```
При необходимости также можно переопределить параметры запуска

```
gradle clean
test/smoke/regress - запуск всех тестов/только smoke/только regress
```

## <img width="4%" style="vertical-align:middle" title="Jenkins" src="images/logo/Jenkins.svg"> <a href="https://jenkins.autotests.cloud/job/QaGuru17-Emelianov-Diplom-Api/">Сборка в Jenkins</a>
<p align="center">
<img title="Jenkins Build" src="images/screenshots/Jenkins.png">
</p>

## <img width="4%" style="vertical-align:middle" title="Allure Report" src="images/logo/Allure_Report.svg"> <a href="https://jenkins.autotests.cloud/job/QaGuru17-Emelianov-Diplom-Api/3/allure/">Пример Allure-отчета</a>
### Overview

<p align="center">
<img title="Allure Overview" src="images/screenshots/allureReportMain.png">
</p>

### Результат выполнения теста

<p align="center">
<img title="Test Results in Alure" src="images/screenshots/allureReportTests.png">
</p>

## <img width="4%" title="Allure TestOPS" src="images/logo/allureTestOps.svg"> Интеграция с [Allure TestOps](https://qameta.io/)

### Основной дашборд

<p align="center">
  <img src="images/screenshots/AllureTestOpsMain.png" alt="dashboard" width="900">
</p>

### Список тестов с результатами прогона

<p align="center">
  <img src="images/screenshots/AllureTestOpsResults.png" alt="dashboard" width="900">
</p>

### Тест-кейсы

<p align="center">
  <img src="images/screenshots/AllureTestOpsTestCases.png" alt="testcase" width="900">
</p>



# Интеграция с Jira
<p align="center">
  <img src="images/screenshots/Jira.png" alt="JiraIntegration" width="950">
</p>

### <img width="4%" style="vertical-align:middle" title="Telegram" src="images/logo/Telegram.svg"> Уведомления в Telegram с использованием бота

После завершения сборки специальный бот, созданный в <code>Telegram</code>, автоматически обрабатывает и отправляет сообщение с отчетом о прогоне.

<p align="center">
<img width="70%" title="Telegram Notifications" src="images/screenshots/Telegram.png">
</p>
