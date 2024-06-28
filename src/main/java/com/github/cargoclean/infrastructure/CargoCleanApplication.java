package com.github.cargoclean.infrastructure;

import com.github.cargoclean.infrastructure.config.CargoCleanProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories
@EnableConfigurationProperties(CargoCleanProperties.class)
public class CargoCleanApplication {

    public static void main(String[] args) {
        SpringApplication.run(CargoCleanApplication.class, args);
    }

}
