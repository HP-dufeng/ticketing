package cloud.fengdu.ticketing.eventstreaming;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import org.springframework.stereotype.Component;

import cloud.fengdu.ticketing.eventstreaming.event.Event;
import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import io.nats.client.PublishOptions;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublisherImpl implements Publisher {

    private final Connection conn;

    @Override
    public void publish(Event event) throws IOException, JetStreamApiException {

        String json = new Gson().toJson(event.getPayload());

        JetStream js = conn.jetStream();

        js.publish(event.getSubject(), json.getBytes(StandardCharsets.UTF_8));

    }

}
