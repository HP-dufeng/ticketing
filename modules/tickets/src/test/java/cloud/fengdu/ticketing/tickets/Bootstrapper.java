package cloud.fengdu.ticketing.tickets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.sql.DataSource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import cloud.fengdu.ticketing.eventregistry.NatsTestServer;
import cloud.fengdu.ticketing.tickets.security.JwtConfig;
import io.nats.client.Options;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
public class Bootstrapper {
    
    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected JwtConfig jwtConfig;
    
    @Autowired
    private DataSource dataSource;

    private static NatsTestServer natsTestServer;

    @BeforeAll
    static void setup() throws IOException {
        natsTestServer = new NatsTestServer(Options.DEFAULT_PORT, false, true);
    }

    @AfterAll 
    static void shutdown() throws InterruptedException {
        natsTestServer.close();
    }

    @AfterEach
    public void cleanupDatabase() throws SQLException {
        Connection conn = dataSource.getConnection();
        Statement statement = conn.createStatement();
   
        // Disable FK
        statement.execute("SET REFERENTIAL_INTEGRITY FALSE");

        // Find all tables and truncate them
        Set<String> tables = new HashSet<>();
        ResultSet resultSet = statement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='PUBLIC'");
        while (resultSet.next()) {
            tables.add(resultSet.getString(1));
        }
        resultSet.close();
        for (String table : tables) {
            statement.executeUpdate("TRUNCATE TABLE " + table);
        }

        // Idem for sequences
        Set<String> sequences = new HashSet<>();
        resultSet = statement.executeQuery("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA='PUBLIC'");
        while (resultSet.next()) {
            sequences.add(resultSet.getString(1));
        }
        resultSet.close();
        for (String seq : sequences) {
            statement.executeUpdate("ALTER SEQUENCE " + seq + " RESTART WITH 1");
        }

        // Enable FK
        statement.execute("SET REFERENTIAL_INTEGRITY TRUE");
        conn.close();
        conn.close();
    }


    protected Cookie signin() {
        String username = "feng@fengdu.cloud";
        Algorithm algorithm =jwtConfig.getAlgorithm();
        String access_token = JWT.create().withSubject(username).sign(algorithm);

        return new Cookie("jwt", access_token);

    }

    protected Cookie signin(String email) {
        String username = email;
        Algorithm algorithm =jwtConfig.getAlgorithm();
        String access_token = JWT.create().withSubject(username).sign(algorithm);

        return new Cookie("jwt", access_token);

    }
}
