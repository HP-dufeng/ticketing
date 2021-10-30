package cloud.fengdu.ticketing.eventstreaming;

import static cloud.fengdu.ticketing.eventstreaming.Subject.TicketCreated;
import static cloud.fengdu.ticketing.eventstreaming.Subject.TicketUpdated;

import java.util.stream.Stream;

public enum NatsStream {
    TICKETING(TicketCreated, TicketUpdated);

    private Subject[] subjects;

    private NatsStream(Subject... subjects) {
        this.subjects = subjects;
    }

    public String[] subjects() {
        return Stream.of(subjects).map(Subject::value).toArray(String[]::new);
    }

}
