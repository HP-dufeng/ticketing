package cloud.fengdu.ticketing.tickets.resource;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cloud.fengdu.ticketing.tickets.domain.entity.Ticket;
import cloud.fengdu.ticketing.tickets.resource.model.CreateTicketRequestDto;
import cloud.fengdu.ticketing.tickets.resource.model.TicketModel;
import cloud.fengdu.ticketing.tickets.resource.model.UpdateTicketRequestDto;
import cloud.fengdu.ticketing.tickets.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketResource {
    
    private final TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<TicketModel> get(@PathVariable Long id) {
        Ticket ticket = ticketService.findById(id);
        if(ticket == null) {
            return ResponseEntity.notFound().build();
        }

        TicketModel ticketModel = new ObjectMapper().convertValue(ticket, TicketModel.class);
        return ResponseEntity.ok(ticketModel);
    }

    @GetMapping
    public ResponseEntity<List<TicketModel>> get() {
        List<Ticket> tickets = ticketService.getAll();
        List<TicketModel> ticketModels = new ObjectMapper().convertValue(tickets,new TypeReference<List<TicketModel>>(){});
        return ResponseEntity.ok(ticketModels);
    }
    

    @PostMapping
    public ResponseEntity<Void> createTicket(@Valid @RequestBody CreateTicketRequestDto CreateTicketRequestDto) {
        Ticket ticketEntity = new ObjectMapper().convertValue(CreateTicketRequestDto, Ticket.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ticketEntity.setUserEmail(authentication.getName());
        ticketService.create(ticketEntity);

        URI location = URI
                .create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tickets").toString());
        
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketModel> updateTicket(@PathVariable Long id, @Valid @RequestBody UpdateTicketRequestDto updateTicketRequestDto) {
        Ticket ticket = ticketService.findById(id);
        if(ticket == null) {
            return ResponseEntity.notFound().build();
        }

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!ticket.getUserEmail().equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ticket.setTitle(updateTicketRequestDto.getTitle());
        ticket.setPrice(updateTicketRequestDto.getPrice());
        ticketService.update(ticket);

        return ResponseEntity.ok().build();
    }
}
