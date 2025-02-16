package com.app.manage_money.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI config() {
        return new OpenAPI()
                .info(new Info()
                        .title("Money Manager API")
                        .description("API for managing incomes and expenses")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Sara Obialero")))
                .addServersItem(new Server()
                        .url("http://localhost:8080/api")
                        .description("Local Development Server"));
    }
}