# Использование базового образа с Java
FROM openjdk:18

# Указание рабочей директории в контейнере
WORKDIR /app

# Копирование исполняемого файла jar в рабочую директорию
COPY Polyclinic-API-0.0.1-SNAPSHOT.jar app.jar

# Команда для запуска приложения
CMD ["java", "-jar", "app.jar"]