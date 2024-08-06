package nbogdan.translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CreateTable implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(CreateTable.class);

    public CreateTable(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(final String... args) {
        log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE IF EXISTS requests");
        jdbcTemplate.execute("CREATE TABLE requests(" +
                "id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, ip inet NOT NULL, query text, result text)");
    }
}
