package io.github.alirostom1.logismart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class LogiSmartApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogiSmartApplication.class, args);
    }

}
