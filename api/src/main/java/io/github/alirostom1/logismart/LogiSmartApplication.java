package io.github.alirostom1.logismart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
@EnableJpaAuditing
public class LogiSmartApplication{
    public static void main(String[] args) {
        SpringApplication.run(LogiSmartApplication.class, args);
    }
}
