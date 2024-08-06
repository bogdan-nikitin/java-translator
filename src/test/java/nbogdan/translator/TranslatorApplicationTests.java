package nbogdan.translator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TranslatorApplicationTests {

	private final TranslatorController controller;
	private final RestTemplate restTemplate;

	@LocalServerPort
	private int port;

    TranslatorApplicationTests(@Autowired final TranslatorController controller,
							   @Autowired final RestTemplateBuilder builder) {
        this.controller = controller;
        this.restTemplate = builder.build();
    }

    @Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void translateOneWord() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/?source={source}&target={target}&query={query}",
				String.class, "en", "ru", "Hello")).contains("Привет");
	}
}
