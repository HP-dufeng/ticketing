package cloud.fengdu.ticketing.eventregistry.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import io.nats.client.Options;

@ConditionalOnClass({ Options.class })
public class NatsConnectionProperties {
    
    /**
	 * URL for the nats server, can be a comma separated list.
	 */
	private String server;

	/**
	 * Connection name, shows up in thread names.
	 */
	private String connectionName;

    public String getServer() {
        return server;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public void setServer(String server) {
        this.server = server;
    }


    public Options.Builder toOptionsBuilder() {
		Options.Builder builder = new Options.Builder();

		builder = builder.server(this.server);
		builder = builder.connectionName(this.connectionName);

		return builder;
	}
    
}
