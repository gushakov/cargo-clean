package com.github.cargoclean.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories
public class CargoCleanApplication {

    public static void main(String[] args) {
        SpringApplication.run(CargoCleanApplication.class, args);
    }

}
