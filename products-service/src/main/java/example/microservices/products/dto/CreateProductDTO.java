package example.microservices.products.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductDTO {

    @NotBlank(message = "El nombre no puede estar en blanco.")
    private String name;

    private String description;

    @NotNull(message = "El precio no puede estar vacío.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que cero.")
    private BigDecimal price;

    @NotNull(message = "El stock no puede estar vacío.")
    @Min(value = 0, message = "El stock no puede ser menor que cero.")
    private int stock;

    @NotNull(message = "El stock reservado no puede estar vacío.")
    @Min(value = 0, message = "El stock reservado no puede ser menor que cero.")
    private int reservedStock;
}
