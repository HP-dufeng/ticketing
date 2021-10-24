package cloud.fengdu.ticketing.auth.resource.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    
    @NotNull
    @Email(message = "Email must be valid")
    private String email;

    @NotNull
    @Length(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    private String password;

}
