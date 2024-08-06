package nbogdan.translator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpRequestTest {
    private final RestTemplate restTemplate;
    private final int port;

    HttpRequestTest(@Autowired final RestTemplateBuilder builder, @LocalServerPort int port) {
        this.restTemplate = builder.build();
        this.port = port;
    }

    @Test
    void translateOneWord() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +
                        "/?source={source}&target={target}&query={query}",
                String.class, "en", "ru", "Hello")).contains("Привет");
    }
}
