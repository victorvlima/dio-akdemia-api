package br.com.akdemia.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Akdemia API")
                        .description("API para gerenciamento de academia - Sistema completo para alunos, instrutores, treinos e matrículas")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipe Akdemia")
                                .email("contato@akdemia.com.brr")
                                .url("https://akdemia.com.brr"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente de Desenvolvimento"),
                        new Server().url("https://api.akdemia.com.brr").description("Ambiente de Produção")
                ));
    }
}