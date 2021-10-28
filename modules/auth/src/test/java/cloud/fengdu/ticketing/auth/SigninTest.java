package cloud.fengdu.ticketing.auth;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.CookieResultMatchers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import cloud.fengdu.ticketing.auth.resource.model.SignupRequestDto;
import cloud.fengdu.ticketing.common.util.JsonUtil;

public class SigninTest extends Bootstrapper {

    @Test
    public void fails_when_a_email_that_does_not_exist_is_supplied() throws Exception {
        Map<String, Object> signinParams = new HashMap<>();
        signinParams.put("email", "feng@fengdu.cloud");
        signinParams.put("password", "password");

        mvc.perform(post("/api/users/signin").contentType(APPLICATION_JSON).content(JsonUtil.toJson(signinParams)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void fails_when_an_incorrect_password_is_supplied() throws Exception {
        SignupRequestDto signupRequestDto = new SignupRequestDto("feng@fengdu.cloud", "password");

        mvc.perform(post("/api/users/signup").contentType(APPLICATION_JSON).content(JsonUtil.toJson(signupRequestDto)))
                .andExpect(status().isCreated());

        Map<String, Object> signinParams = new HashMap<>();
        signinParams.put("email", "feng@fengdu.cloud");
        signinParams.put("password", "dfgdfgfdg");

        mvc.perform(post("/api/users/signin").contentType(APPLICATION_JSON).content(JsonUtil.toJson(signinParams)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void responds_with_a_cookie_given_valid_credentials() throws Exception {
        SignupRequestDto signupRequestDto = new SignupRequestDto("feng@fengdu.cloud", "password");
        mvc.perform(post("/api/users/signup").contentType(APPLICATION_JSON).content(JsonUtil.toJson(signupRequestDto)))
                .andExpect(status().isCreated());

        Map<String, Object> signinParams = new HashMap<>();
        signinParams.put("email", "feng@fengdu.cloud");
        signinParams.put("password", "password");

        mvc.perform(post("/api/users/signin").contentType(APPLICATION_JSON).content(JsonUtil.toJson(signinParams)))
                .andExpect(cookie().exists("jwt"));
    }
}
