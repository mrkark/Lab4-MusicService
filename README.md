# Lab5-MusicService

Лабораторная работа №5 по предмету ИППРПО: Статический анализ исходного кода (Checkstyle) и журналирование приложения (SLF4J + Logback).

## Информация о проекте
- **Предметная область:** Музыкальный сервис (Music Service)
- **Вариант:** 11
- **Индивидуальное задание:** Добавить Logging добавления трека. Успешное добавление фиксировать через INFO, отсутствие трека / плейлиста / дубликат – через WARN, критические ошибки – через ERROR. Продемонстрировать Checkstyle Static Analysis и автоматический конвейер Jenkins.

## Что реализовано в ЛР №5:
1. **Статический анализ кода (Static Analysis):**
   - Настроен `maven-checkstyle-plugin` версии 3.6.0.
   - Создан `checkstyle.xml` с набором правил: соглашения об именовании, отступы, фигурные скобки, длина строк, запрет неиспользуемых и star-импортов.
   - Команда запуска анализа: `mvn checkstyle:check`.
2. **Журналирование (Logging):**
   - Подключены `slf4j-api` (2.0.16) и `logback-classic` (1.5.12).
   - Настроен `src/main/resources/logback.xml`.
   - Внедрен `Logger` в `PlaylistService` и `Main`.
   - Реализованы сценарии `INFO`, `WARN` и `ERROR`.
3. **Интеграция в Jenkins Pipeline:**
   - В `Jenkinsfile` добавлен этап `Static Analysis` между `Build` и `Test`.
   - Полный конвейер: `Checkout` -> `Build` -> `Static Analysis` -> `Test` -> `Resolve Dependencies` -> `Package`.
