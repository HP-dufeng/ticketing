package cloud.fengdu.ticketing.tickets.resource;

import java.net.URI;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cloud.fengdu.ticketing.tickets.domain.entity.Ticket;
import cloud.fengdu.ticketing.tickets.resource.model.TicketModel;
import cloud.fengdu.ticketing.tickets.service.TicketService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketResource {
    
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Void> createTicket(@Valid @RequestBody TicketModel ticketModel) {
        Ticket ticketEntity = new ObjectMapper().convertValue(ticketModel, Ticket.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ticketEntity.setUserEmail(authentication.getName());
        ticketService.create(ticketEntity);

        URI location = URI
                .create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tickets").toString());
        
        return ResponseEntity.created(location).build();
    }
}
