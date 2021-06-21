# zip_download

Simple Project to download zip file. 

## Step 1
Add the dependencies.
```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>${swagger.version}</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>${swagger.version}</version>
        </dependency>
        <dependency>
            <groupId>javax.ws.rs</groupId>
            <artifactId>javax.ws.rs-api</artifactId>
            <version>2.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Step 2
Create a main class.
```java
@Slf4j
@SpringBootApplication
public class ZipDownloadApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ZipDownloadApplication.class);
        Environment environment = springApplication.run(args).getEnvironment();


        log.info("\n----------------------------------------------------------\n\t" +
                        "Application \"ZipDownload\" is running! Access URLs:\n\t" +
                        "Local: http://127.0.0.1:{}/swagger-ui.html\n\t",

                environment.getProperty("server.port"));
    }
}
```

## Step 3
Create a swagger configuration class.
```java
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui.html");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.camelya58.zip_download.controller"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }
}
```

## Step 4
Create a class for generating zip. It's not necessary to close the ByteArrayOutputStream, but other OutputStream must be closed.
```java
@Slf4j
public class ZipUtil {

    public static String TEXT = "Some text in file.";

    public static ByteArrayOutputStream createZip() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ZipOutputStream zip = new ZipOutputStream(baos);
            ZipEntry entry = new ZipEntry("file.txt");
            zip.putNextEntry(entry);
            zip.write(TEXT.getBytes());
            zip.closeEntry();
            zip.close();
        } catch (Exception e) {
            log.error("error = {}, message = {}", e, e.getMessage());
        }
        return baos;
    }
}
```

## Step 5
Create a controller class. The status must be set to ok, otherwise the zip file will not open.
```java
@Slf4j
@RestController
public class ZipController {

    @GetMapping(value = "/getZip", produces = {"application/x-zip-compressed"})
    public ResponseEntity<byte[]> getZip() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .header("Content-Disposition", "attachment; filename=attachment.zip")
                .contentType(new MediaType("application", "x-zip-compressed"))
                .body(ZipUtil.createZip().toByteArray());
    }
}
```

## Step 6
Create a test class.
```java
@SpringBootTest
public class ZipControllerTest {

    @Autowired
    private ZipController controller;

    @Test
    public void getZipTest() {
        ResponseEntity<byte[]> actual = controller.getZip();
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals("attachment.zip", actual.getHeaders().getContentDisposition().getFilename());
        assertEquals(150, Objects.requireNonNull(actual.getBody()).length);
        assertEquals(new MediaType("application", "x-zip-compressed"), actual.getHeaders().getContentType());
    }
}
```

## Step 7
If you want to show error in text you response will be:
```java
ResponseEntity.serverError()
               .header("Access-Control-Allow-Origin", "*")
               .header("Access-Control-Allow-Methods", "GET, OPTIONS")
               .header("Access-Control-Allow-Headers", "Content-Type")
               .header("Content-Disposition", "inline)
               .contentType(new MediaType("application", "text\plain"))
               .body(errorText.getBytes());
```