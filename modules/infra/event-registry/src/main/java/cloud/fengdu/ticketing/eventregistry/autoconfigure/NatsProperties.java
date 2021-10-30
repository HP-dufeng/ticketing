package cloud.fengdu.ticketing.eventregistry.autoconfigure;

import io.nats.client.Options;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConditionalOnClass({ Options.class })
@ConfigurationProperties(prefix = "nats.spring")
public class NatsProperties extends NatsConnectionProperties {
    
}
