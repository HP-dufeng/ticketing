package cloud.fengdu.ticketing.eventstreaming.event;

public interface Event {

    String getSubject();
    
    Object getPayload();
}
