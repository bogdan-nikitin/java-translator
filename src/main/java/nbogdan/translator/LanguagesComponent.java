package nbogdan.translator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LanguagesComponent {
    public static final String API_PATH = "/languages";
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public LanguagesComponent(final RestTemplate restTemplate,
                              @Value("${TRANSLATOR_API_URL}") final String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    @Bean
    public Language[] languages() {
        final var response = restTemplate.getForEntity(apiUrl + API_PATH, Language[].class);
        return response.getBody();
    }
}
