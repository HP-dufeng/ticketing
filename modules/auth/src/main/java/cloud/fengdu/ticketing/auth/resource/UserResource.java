package cloud.fengdu.ticketing.auth.resource;

import java.net.URI;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import cloud.fengdu.ticketing.auth.domain.entity.User;
import cloud.fengdu.ticketing.auth.resource.model.SignupRequestDto;
import cloud.fengdu.ticketing.auth.resource.model.UserModel;
import cloud.fengdu.ticketing.auth.security.JwtUtil;
import cloud.fengdu.ticketing.auth.service.UserService;
import cloud.fengdu.ticketing.common.service.exception.ConflictingRequestException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/currentuser")
    public ResponseEntity<UserModel> currentUser(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, "jwt");
        if (cookie == null) {
            return ResponseEntity.notFound().build();
        }
        String token = cookie.getValue();

        UserModel userModel = jwtUtil.verifyAccessToken(token);

        return ResponseEntity.ok(userModel);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDto dto) {
        URI location = URI
                .create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/signup").toString());

        User user = new User();
        user.setEmailAddress(dto.getEmail());
        user.setPassword(dto.getPassword());
        try {
            userService.saveUser(user);
            
        } catch (DuplicateKeyException e) {
            throw new ConflictingRequestException("User existed");
        }
        return ResponseEntity.created(location).build();
    }
}
