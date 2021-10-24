package cloud.fengdu.ticketing.auth.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String emailAddress;
    private String password;
}
