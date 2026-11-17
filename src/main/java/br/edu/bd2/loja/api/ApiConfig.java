package br.edu.bd2.loja.api;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@OpenAPIDefinition(info = @Info(
        title = "Loja de Eletrônicos E-commerce - API do OLTP",
        version = "1.0",
        description = "Camada REST sobre o sistema transacional (JPA 2.1 / Hibernate 5.2.10). BD II - Etapa 1."))
public class ApiConfig extends ResourceConfig {

    public ApiConfig() {
        register(ClienteResource.class);
        register(ProdutoResource.class);
        register(ErroMapper.class);
        register(JacksonConfig.class);
        register(JacksonFeature.class);
        register(OpenApiResource.class);
    }
}
