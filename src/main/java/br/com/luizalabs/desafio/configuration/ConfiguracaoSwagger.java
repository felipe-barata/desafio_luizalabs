package br.com.luizalabs.desafio.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracaoSwagger {

  @Bean
  public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption, @Value("${application-version}") String appVersion) {
    return new OpenAPI()
        .info(new Info()
            .title("Desafio LuizaLabs")
            .version(appVersion)
            .description(appDesciption)
        );
  }

}
