package cloud.fengdu.ticketing.tickets.security;

import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
public class JwtConfig {
    
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey.getBytes());

    }

}
