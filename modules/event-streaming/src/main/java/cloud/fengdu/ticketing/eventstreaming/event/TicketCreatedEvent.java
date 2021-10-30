package cloud.fengdu.ticketing.eventstreaming.event;

import cloud.fengdu.ticketing.eventstreaming.Subject;

public record TicketCreatedEvent(Payload payload) implements Event {

    @Override
    public String getSubject() {
        return Subject.TicketCreated.value();
    }

    @Override
    public Object getPayload() {
        return this.payload;
    }


    public record Payload(String id, String title, double price, String userEmail) {
    }
}

