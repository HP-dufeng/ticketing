package cloud.fengdu.ticketing.auth.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
public class JwtConfig {
    
    private String secretKey;
    private Integer tokenExpirationAfterDays;

    public String getSecretKey() {
        return secretKey;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    

}
