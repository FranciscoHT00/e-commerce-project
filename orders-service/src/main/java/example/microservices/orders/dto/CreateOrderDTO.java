package example.microservices.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderDTO {

    @NotNull(message = "El id del usuario no puede estar vacío.")
    private Long userId;

    @NotNull(message = "El id del producto no puede estar vacío.")
    private String productId;

    @NotNull(message = "La cantidad no puede estar vacía.")
    @Min(value = 1, message = "La cantidad no puede ser menor a uno.")
    private int quantity;

    private String status;
}
