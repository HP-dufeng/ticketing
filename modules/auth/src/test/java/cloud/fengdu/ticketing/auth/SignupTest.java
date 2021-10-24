package cloud.fengdu.ticketing.auth;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import cloud.fengdu.ticketing.auth.resource.model.SignupRequestDto;
import cloud.fengdu.ticketing.auth.util.JsonUtil;


public class SignupTest extends Bootstrapper {
    
    
    @Test
    public void returns_a_201_on_successful_signup_Test() throws Exception {
        SignupRequestDto dto = new SignupRequestDto("feng@fengdu.cloud", "password");

        mvc.perform(post("/api/users/signup").contentType(APPLICATION_JSON).content(JsonUtil.toJson(dto)))
            .andExpect(status().isCreated());
            
    }

    @Test
    public void returns_a_400_with_an_invalid_email() throws Exception {
        SignupRequestDto dto = new SignupRequestDto("fengengdoud", "password");

        mvc.perform(post("/api/users/signup").contentType(APPLICATION_JSON).content(JsonUtil.toJson(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void returns_a_400_with_an_invalid_password() throws Exception {
        SignupRequestDto dto = new SignupRequestDto("feng@fengdu.cloud", "p");

        mvc.perform(post("/api/users/signup").contentType(APPLICATION_JSON).content(JsonUtil.toJson(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void returns_a_400_with_missing_email_and_password() throws Exception {
        SignupRequestDto dto = new SignupRequestDto();

        mvc.perform(post("/api/users/signup").contentType(APPLICATION_JSON).content(JsonUtil.toJson(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void disallows_duplicate_emails() throws Exception {
        SignupRequestDto dto = new SignupRequestDto("feng@fengdu.cloud", "password");

        mvc.perform(post("/api/users/signup").contentType(APPLICATION_JSON).content(JsonUtil.toJson(dto)))
            .andExpect(status().isCreated());

            mvc.perform(post("/api/users/signup").contentType(APPLICATION_JSON).content(JsonUtil.toJson(dto)))
            .andExpect(status().isConflict());
    }
}
