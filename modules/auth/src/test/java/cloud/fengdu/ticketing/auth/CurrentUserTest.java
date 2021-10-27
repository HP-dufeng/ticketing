package cloud.fengdu.ticketing.auth;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.CookieResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import cloud.fengdu.ticketing.auth.resource.model.SignupRequestDto;
import cloud.fengdu.ticketing.auth.resource.model.UserModel;
import cloud.fengdu.ticketing.auth.util.JsonUtil;

public class CurrentUserTest extends Bootstrapper {
    
    @Test
    public void responds_with_details_about_the_current_user() throws Exception {

        SignupRequestDto signupRequestDto = new SignupRequestDto("feng@fengdu.cloud", "password");
        mvc.perform(post("/api/users/signup").contentType(APPLICATION_JSON).content(JsonUtil.toJson(signupRequestDto)))
                .andExpect(status().isCreated());

        Map<String, Object> signinParams = new HashMap<>();
        signinParams.put("email", "feng@fengdu.cloud");
        signinParams.put("password", "password");

        
        MvcResult signinResult = mvc.perform(post("/api/users/signin").contentType(APPLICATION_JSON).content(JsonUtil.toJson(signinParams)))
                .andReturn();
        assertNotNull(signinResult.getResponse().getCookie("jwt"));

                UserModel currentUserModel = new UserModel("feng@fengdu.cloud", new ArrayList<>());
        mvc.perform(get("/api/users/currentuser").cookie(signinResult.getResponse().getCookie("jwt")).contentType(APPLICATION_JSON))
            .andExpect(content().json(JsonUtil.toJsonString(currentUserModel)));
    }
}
