package com.intabia.spring.boot.starter.minio;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для взаимодействия с MinIO Client
 */
@Slf4j
@RequiredArgsConstructor
public class MinIOService {

    private final MinioClient minioClient;

    /**
     * Получение всех файлов в хранилище
     *
     * @param bucketName - наименование бакета в хранилище
     * @return список всех файлов в хранилище
     */
    @SneakyThrows
    public Iterable<Result<Item>> getFiles(String bucketName) {
        log.info("Получение всех файлов в хранилище");
        return minioClient.listObjects(bucketName);
    }

    /**
     * Получение файла из хранилища
     *
     * @param bucketName - наименование бакета в хранилище
     * @param filePath   - наименование пути до файла в хранилище
     * @return поток байтов
     */
    @SneakyThrows
    public InputStream getFile(String bucketName, String filePath) {
        return minioClient.getObject(bucketName, filePath);
    }

    /**
     * Сохрание файла в хранилище
     *
     * @param bucketName - наименование бакета в хранилище
     * @param filePath   - наименование пути до файла в хранилище
     * @param file       - файл
     * @return ссылка на файл в хранилище
     */
    @SneakyThrows
    public String saveFile(String bucketName, String filePath, MultipartFile file) {
        log.info("Проверка наличия бакета");
        makeBucket(bucketName);
        log.info("Сохрание файла в хранилище");
        minioClient.putObject(bucketName, filePath, file.getInputStream(), file.getContentType());
        return filePath;
    }

    /**
     * Сохрание файла в хранилище
     *
     * @param bucketName  - наименование бакета в хранилище
     * @param filePath    - наименование пути до файла в хранилище
     * @param contentType - тип передаемого контента
     * @param inputStream - входящий поток
     * @return ссылка на файл в хранилище
     */
    @SneakyThrows
    public String saveFile(String bucketName, String filePath, String contentType, InputStream inputStream) {
        log.info("Проверка наличия бакета");
        makeBucket(bucketName);
        log.info("Сохрание файла в хранилище");
        minioClient.putObject(bucketName, filePath, inputStream, inputStream.available(), contentType);
        return filePath;
    }

    /**
     * Копирование файла в хранилище
     *
     * @param bucketName   - наименование бакета в хранилище
     * @param filePath     - наименование пути до файла в хранилище
     * @param destFilePath - наименование пути для копирования файла в хранилище
     */
    @SneakyThrows
    public void copyFile(String bucketName, String filePath, String destFilePath) {
        log.info("Проверка наличия бакета");
        makeBucket(bucketName);
        log.info("Сохрание файла в хранилище");
        minioClient.copyObject(bucketName, filePath, destFilePath);
    }

    /**
     * Удаление файла в хранилище
     *
     * @param bucketName - наименование бакета в хранилище
     * @param filePath   - наименование пути до файла в хранилище
     */
    @SneakyThrows
    public void removeFile(String bucketName, String filePath) {
        log.info("Удаление файла в хранилище");
        minioClient.removeObject(bucketName, filePath);
    }

    /**
     * Создание бакета в хранилище
     *
     * @param bucketName - наименование бакета в хранилище
     */
    @SneakyThrows
    public void makeBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            log.info("Создание бакета в хранилище");
            minioClient.makeBucket(bucketName);
        }
    }

    /**
     * Получение списка наименований всех бакетов в хранилище
     *
     * @return список наименований всех бакетов в хранилище
     */
    @SneakyThrows
    public List<String> getBuckets() {
        log.info("Получение списка наименований всех бакетов в хранилище");
        return minioClient.listBuckets().stream().map(Bucket::name).collect(Collectors.toList());
    }

    /**
     * Удаление бакета в хранилище
     *
     * @param bucketName - наименование бакета в хранилище
     */
    @SneakyThrows
    public void removeBucket(String bucketName) {
        if (bucketExists(bucketName)) {
            log.info("Удаление бакета в хранилище");
            minioClient.removeBucket(bucketName);
        }
    }

    /**
     * Проверка наличия бакета в хранилище
     *
     * @param bucketName - наименование бакета в хранилище
     * @return - true/false
     */
    @SneakyThrows
    private boolean bucketExists(String bucketName) {
        log.info("Проверка наличия бакета в хранилище");
        return minioClient.bucketExists(bucketName);
    }
}