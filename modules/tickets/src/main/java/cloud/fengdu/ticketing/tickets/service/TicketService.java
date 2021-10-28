package cloud.fengdu.ticketing.tickets.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cloud.fengdu.ticketing.tickets.domain.entity.Ticket;

public interface TicketService {
    
    void create(Ticket entity);

    Ticket findById(long entityId);

    List<Ticket> getAll();

    void update(Ticket ticket);
}
