package cloud.fengdu.ticketing.eventstreaming.event;

import cloud.fengdu.ticketing.eventstreaming.Subject;

public record TicketUpdatedEvent(Payload payload) implements Event {

    @Override
    public String getSubject() {
        return Subject.TicketUpdated.value();
    }

    @Override
    public Object getPayload() {
        return this.payload;
    }


    public record Payload(String id, String title, double price, String userEmail) {
    }
}

