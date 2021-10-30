package cloud.fengdu.ticketing.eventstreaming;

import cloud.fengdu.ticketing.eventstreaming.event.Event;
import cloud.fengdu.ticketing.eventstreaming.event.TicketCreatedEvent;
import cloud.fengdu.ticketing.eventstreaming.event.TicketUpdatedEvent;

public abstract class Events {
    
    public static Event ticketCreated(String id, String title, double price, String userEmail) {
        return new TicketCreatedEvent(new TicketCreatedEvent.Payload(id, title, price, userEmail));
    }

    public static Event ticketUpdated(String id, String title, double price, String userEmail) {
        return new TicketUpdatedEvent(new TicketUpdatedEvent.Payload(id, title, price, userEmail));
    }
}
