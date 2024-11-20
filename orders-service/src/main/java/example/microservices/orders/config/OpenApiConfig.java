package example.microservices.orders.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Orders Service API",
                version = "1.0",
                description = "API documentation for managing orders"
        ),
        servers = {
                @Server(url = "http://localhost:8765/orders-service", description = "API Gateway")
        }
)
public class OpenApiConfig {
    // This class can be empty, just add the annotations as shown.
}
