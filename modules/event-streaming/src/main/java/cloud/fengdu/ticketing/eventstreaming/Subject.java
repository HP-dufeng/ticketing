package cloud.fengdu.ticketing.eventstreaming;



import java.util.stream.Stream;

public enum Subject {
    TicketCreated("ticket.created"),
    TicketUpdated("ticket.updated");

    private String value;

    Subject(String subject) {
        this.value = subject;
    }

    public String value() {
        return value;
    }

    public static Stream<Subject> stream() {
        return Stream.of(Subject.values());
    }

}

