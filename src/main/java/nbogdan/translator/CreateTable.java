package nbogdan.translator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateTable implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

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
