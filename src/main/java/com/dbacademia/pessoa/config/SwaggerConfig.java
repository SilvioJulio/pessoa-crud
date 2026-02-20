package com.dbacademia.pessoa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gerenciamento de Pessoas")
                        .version("2.0")
                        .description("Sistema para cadastro e consulta de pessoas - DB Academia")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@dbacademia.com")));
    }
}
