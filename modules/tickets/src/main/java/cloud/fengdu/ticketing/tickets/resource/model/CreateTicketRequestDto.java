package cloud.fengdu.ticketing.tickets.resource.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketRequestDto {
    
    @NotEmpty
    @NotNull(message = "Title is required")
    private String title;

    @DecimalMin(value = "1.0", message = "Price must be greater than 1")
    private double price;
}
