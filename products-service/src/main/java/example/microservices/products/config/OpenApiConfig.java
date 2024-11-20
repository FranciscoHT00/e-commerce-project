package example.microservices.products.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Products Service API",
                version = "1.0",
                description = "API documentation for managing products"
        ),
        servers = {
                @Server(url = "http://localhost:8765/products-service", description = "API Gateway")
        }
)
public class OpenApiConfig {
    // This class can be empty, just add the annotations as shown.
}
