package ru.praktikum.kanban.repository.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import ru.praktikum.kanban.exception.TaskFileStorageException;
import ru.praktikum.kanban.model.backed.file.TasksBackup;

public class TaskFileStorage {

    private static final String FILE_FIRST_LINE = "id,type,name,description,status,epic";
    private static final String FILE_PREFIX = "tasks";
    private static final String FILE_SUFFIX = ".csv";

    Path filePath;
    TasksCsvWriter writer;
    TasksCsvReader reader;

    public TaskFileStorage(TasksCsvWriter writer, TasksCsvReader reader) {
        String absolutePath = "";
        try {
            File file = File.createTempFile(FILE_PREFIX, FILE_SUFFIX);
            absolutePath = file.getAbsolutePath();
            this.filePath = Paths.get(absolutePath);
        } catch (IOException ignored) {
            System.out.println("Произошла ошибка при создании файла. Используется директория приложения");
        }
        if (absolutePath.isBlank()) {
            this.filePath = Paths.get(absolutePath, FILE_PREFIX + FILE_SUFFIX);
        }
        System.out.println("Создан файл: " + filePath.toAbsolutePath());
        this.writer = writer;
        this.reader = reader;
    }

    public TasksBackup getBackup() throws TaskFileStorageException {
        if (!isFileExists()) {
            return new TasksBackup();
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toString()))) {
            TasksBackup backup = reader.read(bufferedReader);
            bufferedReader.close();
            return backup;
        } catch (IOException e) {
            throw new TaskFileStorageException("Произошла ошибка во время чтения файла.", e);
        }
    }

    public void save(TasksBackup backup)
            throws TaskFileStorageException {
        createFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath.toString(), true))) {
            writer.write(backup, bufferedWriter);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new TaskFileStorageException("Произошла ошибка во время записи файла.", e);
        }
    }

    private void createFile() throws TaskFileStorageException {
        try {
            Files.writeString(filePath, FILE_FIRST_LINE);
            System.out.println("Перезаписан файл: " + filePath);
        } catch (IOException e) {
            throw new TaskFileStorageException("Произошла ошибка при перезаписи файла.", e);
        }
    }

    private boolean isFileExists() {
        return Files.exists(filePath);
    }
}
