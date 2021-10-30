package cloud.fengdu.ticketing.tickets;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import cloud.fengdu.ticketing.tickets.domain.entity.Ticket;
import cloud.fengdu.ticketing.tickets.domain.repository.TicketRepository;
import io.nats.client.Options;
import cloud.fengdu.ticketing.common.util.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateTicketTest extends Bootstrapper {
    
    
    @Test
    public void has_a_resource_api_listening_for_create_ticket() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/tickets")).andReturn();

        assertNotEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void can_only_by_accessed_if_the_user_is_signed_in() throws Exception {
        mvc.perform(post("/api/tickets")).andExpect(status().isForbidden());
    }

    @Test
    public void returns_a_status_other_than_403_if_the_user_is_signed_in() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/tickets").cookie(signin())).andReturn();

        assertNotEquals(HttpStatus.FORBIDDEN.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void returns_an_error_if_an_invalid_title_is_provided() throws Exception {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("title", "");
        requestParams.put("price", 10);
        mvc.perform(
            post("/api/tickets")
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isBadRequest());

        requestParams = new HashMap<>();
        requestParams.put("price", 10);
        mvc.perform(
            post("/api/tickets")
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isBadRequest());

        
    }

    @Test
    public void returns_an_error_if_an_invalid_price_is_provided() throws Exception {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("title", "sdfsdf");
        requestParams.put("price", -10);
        mvc.perform(
            post("/api/tickets")
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isBadRequest());

        requestParams = new HashMap<>();
        requestParams.put("title", "sdfsdf");
        mvc.perform(
            post("/api/tickets")
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isBadRequest());

        requestParams = new HashMap<>();
        requestParams.put("title", "sdfsdf");
        requestParams.put("price", 0.5);
        mvc.perform(
            post("/api/tickets")
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isBadRequest());
    }

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void creates_ticket_with_valid_inputs() throws Exception {

        List<Ticket> tickets = ticketRepository.getAll();
        assertTrue(tickets.size() == 0);
        
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("title", "sdfsdf");
        requestParams.put("price", 10);
        mvc.perform(
            post("/api/tickets")
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isCreated());

        tickets = ticketRepository.getAll();
        assertTrue(tickets.size() == 1);
    }
}
