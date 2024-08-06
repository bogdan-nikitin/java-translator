package nbogdan.translator.api;

import lombok.extern.slf4j.Slf4j;
import nbogdan.translator.api.dto.Language;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
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
        } catch (final TranslateException e) {
            log.error("Error during retrieving languages: {}. Shutting down...", e.getMessage());
            ctx.close();
            return null;
        }
    }
}
