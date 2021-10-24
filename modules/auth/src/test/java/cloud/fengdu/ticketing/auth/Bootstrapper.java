package cloud.fengdu.ticketing.auth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
public class Bootstrapper {
    
    @Autowired
    protected MockMvc mvc;
    
    @Autowired
    private DataSource dataSource;

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


}
