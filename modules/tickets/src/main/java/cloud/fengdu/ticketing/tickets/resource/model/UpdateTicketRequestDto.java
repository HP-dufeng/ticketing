package cloud.fengdu.ticketing.tickets.resource.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketRequestDto {
    
    @NotNull
    private Long id;
    
    @NotEmpty
    @NotNull(message = "Title is required")
    private String title;

    @DecimalMin(value = "1.0", message = "Price must be greater than 1")
    private double price;

}
