package cloud.fengdu.ticketing.eventstreaming.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cloud.fengdu.ticketing.eventstreaming.Subject;
import cloud.fengdu.ticketing.eventstreaming.nats.NatsJsUtils;
import io.nats.client.Connection;
import io.nats.client.ConnectionListener;
import io.nats.client.Consumer;
import io.nats.client.ErrorListener;

import static cloud.fengdu.ticketing.eventstreaming.NatsStream.TICKETING;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Configuration
public class EventStreamConfiguration {

    private static final Log logger = LogFactory.getLog(EventStreamConfiguration.class);
    

    @Bean
    public ConnectionListener createConnectionListener() {
        return new ConnectionListener() {
            @Override
            public void connectionEvent(Connection conn, Events type) {
                try {
                    
                    NatsJsUtils.createStreamOrUpdateSubjects(conn, TICKETING.name(), TICKETING.subjects());
                } catch (Exception e) {
                    logger.error("## error occurred when create stream", e);
                }
                
                logger.info("## NATS Connection status change " + type);
            }
        };
    }

    @Bean
    public ErrorListener createErrorListener() {
        return new ErrorListener() {
            @Override
            public void slowConsumerDetected(Connection conn, Consumer consumer) {
                logger.info("## slow consumer detected");
            }

            @Override
            public void exceptionOccurred(Connection conn, Exception exp) {
                logger.info("## exception occurred");
                exp.printStackTrace();
            }

            @Override
            public void errorOccurred(Connection conn, String error) {
                logger.info("## error occurred " + error);
            }
        };
    }
}
