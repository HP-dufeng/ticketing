package cloud.fengdu.ticketing.auth.resource.model;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserModel {
    private final String email;
    private final List<String> roles;
}
