package nbogdan.translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Service
public class TranslateService {
    private static final int THREADS_COUNT = 10;
    private static final Logger log = LoggerFactory.getLogger(TranslateService.class);
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final static String API_PATH = "/translate";
    private final ExecutorService executorService;

    public TranslateService(final RestTemplate restTemplate, @Value("${TRANSLATOR_API_URL}") final String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.executorService = Executors.newFixedThreadPool(THREADS_COUNT);
    }

    public String translateWord(final String source, final String target, final String word) throws TranslateException {
        final TranslatedText response = restTemplate.postForObject(
                apiUrl + API_PATH, new TranslateQuery(source, target, word), TranslatedText.class);

        return Objects.requireNonNull(response).getTranslatedText();
    }

    public String translate(final String source, final String target, final String query) throws TranslateException {
        final Stream<String> words = BreakWords.breakWords(query, Locale.of(source));
        final StringBuilder result = new StringBuilder();
        try {
            for (final Future<String> future : executorService.invokeAll(words
                    .map(word -> (Callable<String>)
                            () -> BreakWords.isWord(word) ? translateWord(source, target, word) : word)
                    .toList())) {
                result.append(future.get());
            }
        } catch (final InterruptedException e) {
            log.warn("Translation thread interrupted");
            throw new TranslateException("Server error", e);
        } catch (final ExecutionException e) {
            if (e.getCause() instanceof TranslateException translateException) {
                throw translateException;
            }
            log.warn("Unknown exception during processing API call: {}", e.getMessage());
            throw new TranslateException("API error", e.getCause());
        }
        return result.toString();
    }
}
