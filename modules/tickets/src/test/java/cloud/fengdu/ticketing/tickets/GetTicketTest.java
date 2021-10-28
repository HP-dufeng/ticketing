package cloud.fengdu.ticketing.tickets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import cloud.fengdu.ticketing.common.util.JsonUtil;
import cloud.fengdu.ticketing.tickets.domain.entity.Ticket;
import cloud.fengdu.ticketing.tickets.domain.repository.TicketRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GetTicketTest extends Bootstrapper {

    @Test
    public void returns_404_if_the_ticket_is_not_found() throws Exception {
        mvc.perform(get("/api/tickets/1111"))
            .andExpect(status().isNotFound());   
    }

    @Test
    public void returns_400_if_the_ticket_id_is_invalid() throws Exception {
        mvc.perform(get("/api/tickets/sadfasf"))
            .andExpect(status().isBadRequest());   
    }

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void returns_the_ticket_if_the_ticket_is_found() throws Exception {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("title", "sdfsdf");
        requestParams.put("price", 10);
        mvc.perform(
            post("/api/tickets")
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isCreated());

        Ticket ticket = ticketRepository.getAll().get(0);

        mvc.perform(get(String.format("/api/tickets/%s", ticket.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(requestParams.get("title")))
            .andExpect(jsonPath("$.price").value(requestParams.get("price")));

    }

    @Test
    public void can_fetch_a_list_of_tickets() throws Exception {
        createTicket();
        createTicket();
        createTicket();
    
        mvc.perform(get("/api/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(3));
    }

    private void createTicket() throws Exception {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("title", "sdfsdf");
        requestParams.put("price", 10);
        mvc.perform(
            post("/api/tickets")
                .cookie(signin())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(requestParams))
            ).andExpect(status().isCreated());
    }
}
