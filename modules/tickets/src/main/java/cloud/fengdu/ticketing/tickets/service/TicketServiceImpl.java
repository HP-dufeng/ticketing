package cloud.fengdu.ticketing.tickets.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cloud.fengdu.ticketing.tickets.domain.entity.Ticket;
import cloud.fengdu.ticketing.tickets.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    
    @Override
    public void create(Ticket entity) {
        ticketRepository.create(entity);
    }

    @Override
    public Ticket findById(long entityId) {
        return ticketRepository.findById(entityId);
    }

    @Override
    public List<Ticket> getAll() {
        return ticketRepository.getAll();
    }
    
}
