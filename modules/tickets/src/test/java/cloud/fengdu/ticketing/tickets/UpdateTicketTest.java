package cloud.fengdu.ticketing.tickets;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import cloud.fengdu.ticketing.common.util.JsonUtil;
import cloud.fengdu.ticketing.tickets.domain.entity.Ticket;
import cloud.fengdu.ticketing.tickets.domain.repository.TicketRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UpdateTicketTest extends Bootstrapper {
    
    @Test
    public void returns_a_404_if_the_provided_id_does_not_exist() throws Exception {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("id", 111);
        requestParams.put("title", "sdfsdf");
        requestParams.put("price", 23);

        mvc.perform(
            put(String.format("/api/tickets/%s", requestParams.get("id")))
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void returns_a_403_if_the_user_is_not_authenticated() throws Exception {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("id", 111);
        requestParams.put("title", "sdfsdf");
        requestParams.put("price", 23);

        mvc.perform(
            put(String.format("/api/tickets/%s", requestParams.get("id")))
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams)))
            .andExpect(status().isForbidden());
    }



    @Autowired
    private TicketRepository ticketRepository;
    
    @Test
    public void returns_a_403_if_the_user_does_not_own_the_ticket() throws Exception {
        // Create a ticket 
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("title", "sdfsdf");
        requestParams.put("price", 10);
        mvc.perform(
            post("/api/tickets")
                .cookie(signin("feng@fengdu.cloud"))
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isCreated());

        Ticket ticket = ticketRepository.getAll().get(0);

        // Test update ticket
        requestParams = new HashMap<>();
        requestParams.put("id", ticket.getId());
        requestParams.put("title", "sdfsdf");
        requestParams.put("price", 23);

        mvc.perform(
            put(String.format("/api/tickets/%s", requestParams.get("id")))
                .cookie(signin("another@fengdu.cloud"))
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams)))
            .andExpect(status().isForbidden());

    }

    @Test
    public void returns_a_400_if_the_user_provides_an_invalid_title_or_price() throws Exception {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("id", 111);

        // invalid title
        requestParams.put("title", "");
        requestParams.put("price", 23);
        mvc.perform(
            put(String.format("/api/tickets/%s", requestParams.get("id")))
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams)))
            .andExpect(status().isBadRequest());

        // invalid price
        requestParams.put("title", "adsfsdf");
        requestParams.put("price", -23);
        mvc.perform(
            put(String.format("/api/tickets/%s", requestParams.get("id")))
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updates_the_ticket_provided_valid_inputs() throws Exception {
        // Create a ticket 
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("title", "abc");
        requestParams.put("price", 10);
        mvc.perform(
            post("/api/tickets")
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isCreated());

        Ticket ticket = ticketRepository.getAll().get(0);

        // Test update ticket
        requestParams.put("id", ticket.getId());
        requestParams.put("title", "hdff");
        requestParams.put("price", 23);
        mvc.perform(
            put(String.format("/api/tickets/%s", requestParams.get("id")))
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams)))
            .andExpect(status().isOk());

        // Check updated
        mvc.perform(
            get(String.format("/api/tickets/%s", requestParams.get("id"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(requestParams.get("title")))
            .andExpect(jsonPath("$.price").value(requestParams.get("price")));

    }

}
