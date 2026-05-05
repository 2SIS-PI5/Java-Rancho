package sptech.school.projeto_rancho.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gerenciamento de Funcionários Freelancers")
                        .version("1.0.0")
                        .description("Sistema de gerenciamento completo de funcionários freelancers com funcionalidades de cadastro, " +
                                "consulta, atualização e exclusão. A API permite gerenciar informações de contato, áreas de atuação, " +
                                "valores de diária e dados de transporte."));
    }
}
