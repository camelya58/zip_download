package com.github.camelya58.zip_download;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

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
