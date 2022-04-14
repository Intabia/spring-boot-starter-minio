# Spring Boot Starter MinIO

Spring Boot Starter для взаимодействия с файловым хранилищем [MinIO](https://docs.min.io/docs/minio-quickstart-guide.html)

### При использовании стартера, необходимо в файле настроек указать:

```yaml
spring:  
  minio:
    url: MINIO_URL
    access-key: MINIO_ACCESS_KEY
    secret-key: MINIO_SECRET_KEY
```

### Пример использования:

- Добавить зависимость в Maven:

```xml
<dependency>
    <groupId>com.intabia</groupId>
    <artifactId>spring-boot-starter-minio</artifactId>
    <version>1.0.0</version>
</dependency>
```

- Добавить зависимость в Gradle:

```groovy
implementation("ru.dnx:spring-boot-starter-minio:1.0.0")
```

```kotlin
/**
 * Сервис для взаимодействия с файловым хранилищем
 */
@Service
class FileStorageService(
    private val minIOService: MinIOService
) {

    private companion object : KLogging()

    /**
     * Запрос файлов из файлового хранилища
     * @param folderName - наименование папки
     * @param fileName - наименование файла
     * @return файл
     */
    fun retrieveFile(folderName: String, fileName: String): File {
        logger.info { "Запрос файлов из файлового хранилища" }
        val file = File(fileName)
        FileUtils.copyInputStreamToFile(
            minIOService.getFile(
                "accreditation", String.format("%s/%s", folderName, fileName)
            ), file
        )
        return file
    }

    /**
     * Сохранение файла в файловом хранилище и получение ссылки на файл
     * @param file - файл
     * @return ссылка на файл в файловом хранилище
     */
    fun saveFile(file: MultipartFile): String {
        logger.info { "Загрузка файла в файловое хранилище" }
        val folderName: String = UUID.randomUUID().toString()
        val fileName: String = file.name
        return minIOService.saveFile("accreditation", String.format("%s/%s", folderName, fileName), file)
    }
}
```

### Запуск MinIO в Docker

```yaml
version: '3.7'

services:
  minio:
    image: minio/minio:latest
    command: server --console-address ":9001" /data/
    ports:
      - "9000:9000"
      - "9001:9001"
    environment:
      MINIO_ROOT_USER: MINIO_ACCESS_KEY
      MINIO_ROOT_PASSWORD: MINIO_SECRET_KEY
    volumes:
      - minio-storage:/data
volumes:
  minio-storage:
```
