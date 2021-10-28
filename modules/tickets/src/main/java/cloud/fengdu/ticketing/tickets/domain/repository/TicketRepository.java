package cloud.fengdu.ticketing.tickets.domain.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cloud.fengdu.ticketing.tickets.domain.entity.Ticket;

@Mapper
public interface TicketRepository {
    
    void create(Ticket entity);

    Ticket findById(long entityId);

    List<Ticket> getAll();
}
