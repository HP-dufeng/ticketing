package cloud.fengdu.ticketing.tickets.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cloud.fengdu.ticketing.common.service.exception.InternalServerErrorException;
import cloud.fengdu.ticketing.eventstreaming.Events;
import cloud.fengdu.ticketing.eventstreaming.Publisher;
import cloud.fengdu.ticketing.eventstreaming.event.Event;
import cloud.fengdu.ticketing.tickets.domain.entity.Ticket;
import cloud.fengdu.ticketing.tickets.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final Publisher publisher;
    
    @Override
    public void create(Ticket entity) {
        // TODO: Use the outbox pattern or others to ensure that the transaction
        ticketRepository.create(entity);
        try {
            
            Event ticketCreated = Events.ticketCreated(entity.getId(), entity.getTitle(), entity.getPrice(), entity.getUserEmail());
            publisher.publish(ticketCreated);
                        
        }catch (Exception e) {
           throw new InternalServerErrorException("Error occurred when publish event", e);
        }
    }

    @Override
    public Ticket findById(long entityId) {
        return ticketRepository.findById(entityId);
    }

    @Override
    public List<Ticket> getAll() {
        return ticketRepository.getAll();
    }

    @Override
    public void update(Ticket ticket) {
        // TODO: Use the outbox pattern or others to ensure that the transaction
        ticketRepository.update(ticket);
        try {
            
            Event ticketUpdated = Events.ticketUpdated(ticket.getId(), ticket.getTitle(), ticket.getPrice(), ticket.getUserEmail());
            publisher.publish(ticketUpdated);
                        
        }catch (Exception e) {
           throw new InternalServerErrorException("Error occurred when publish event", e);
        }
        
    }
    
}
