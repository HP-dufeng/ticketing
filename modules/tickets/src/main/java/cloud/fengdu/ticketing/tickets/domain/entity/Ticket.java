package cloud.fengdu.ticketing.tickets.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    private String id;
    private String title;
    private double price;
    private String userEmail;
}
