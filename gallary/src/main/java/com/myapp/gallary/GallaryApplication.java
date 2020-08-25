package com.myapp.gallary;

import com.myapp.gallary.utility.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class GallaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(GallaryApplication.class, args);
    }

    @Bean
    Path path(){
        return Paths.get(System.getProperty("java.io.tmpdir"));
    }

    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
