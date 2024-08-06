package nbogdan.translator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class LanguagesComponent {
    public static final String API_PATH = "/languages";
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final ConfigurableApplicationContext ctx;

    public LanguagesComponent(final RestTemplate restTemplate,
                              @Value("${TRANSLATOR_API_URL}") final String apiUrl,
                              final ConfigurableApplicationContext ctx) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.ctx = ctx;
    }

    @Bean
    public Language[] languages() {
        try {
            return restTemplate.getForObject(apiUrl + API_PATH, Language[].class);
        } catch (final HttpStatusCodeException e) {
            final TranslateApiError error = e.getResponseBodyAs(TranslateApiError.class);
            if (error == null || error.getError() == null) {
                log.error("API returned invalid response while retrieving languages. Message: {}. Status code {}",
                        e.getMessage(), e.getStatusCode());
            } else {
                log.error("API returned error while retrieving languages: {}. Status code {}",
                        error.getError(), e.getStatusCode());
            }
            ctx.close();
            return null;
        }
    }
}
