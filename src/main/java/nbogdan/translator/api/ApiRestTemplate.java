package nbogdan.translator.api;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiRestTemplate {
    private final RestTemplate restTemplate;

    public ApiRestTemplate(final RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
        this.restTemplate.setErrorHandler(new ApiErrorHandler(this.restTemplate.getMessageConverters()));
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplate;
    }
}
