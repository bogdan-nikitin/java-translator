package nbogdan.translator;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateComponent {
    private final RestTemplate restTemplate;

    public RestTemplateComponent(final RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplate;
    }
}
