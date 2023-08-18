package info.revington;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("SpringDoc")
                .description("Revington.info web application")
                .version("v0.0.1"));
    }

}
