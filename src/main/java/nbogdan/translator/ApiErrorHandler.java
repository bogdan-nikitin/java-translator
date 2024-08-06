package nbogdan.translator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ApiErrorHandler extends DefaultResponseErrorHandler {

    private final List<HttpMessageConverter<?>> messageConverters;

    public ApiErrorHandler(final List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Override
    public void handleError(@NonNull final ClientHttpResponse response) throws IOException {

        final HttpMessageConverterExtractor<TranslateApiError> errorMessageExtractor =
                new HttpMessageConverterExtractor<>(TranslateApiError.class, messageConverters);

        try {
            final TranslateApiError error = errorMessageExtractor.extractData(response);
            if (error == null || error.getError() == null) {
                log.warn("API returned invalid or null response. Status code {}", response.getStatusCode());
                throw new TranslateException("API error");
            } else {
                log.warn("API returned error: {}. Status code {}",
                        error.getError(), response.getStatusCode());
                throw new TranslateException(error.getError());
            }
        } catch (final RestClientException e) {
            log.warn("Unknown error while accessing API: {}. Status code {}",
                    e.getMessage(), response.getStatusCode());
            throw new TranslateException("API error", e);
        }
    }
}
