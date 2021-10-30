package cloud.fengdu.ticketing.eventstreaming;

import cloud.fengdu.ticketing.eventstreaming.event.Event;

public interface Publisher {
    
    void publish(Event event) throws Exception;
}
