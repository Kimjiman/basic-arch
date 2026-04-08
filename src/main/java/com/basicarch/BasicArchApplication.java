package com.basicarch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@EnableCaching
public class BasicArchApplication extends SpringBootServletInitializer {
    private static final Class<?> app = BasicArchApplication.class;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SpringApplication springApplication = new SpringApplication(app);
        springApplication.addListeners(new ApplicationPidFileWriter("app.pid"));
        springApplication.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(app);
    }

}
