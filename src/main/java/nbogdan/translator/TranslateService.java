package nbogdan.translator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TranslateService {
    private static final int THREADS_COUNT = 10;
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final static String API_PATH = "/translate";
    private final ExecutorService executorService;

    public TranslateService(final RestTemplate restTemplate, @Value("${TRANSLATOR_API_URL}") final String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.executorService = Executors.newFixedThreadPool(THREADS_COUNT);
    }

    public String translateWord(final String source, final String target, final String word) {
        final ResponseEntity<TranslatedText> response = restTemplate.postForEntity(apiUrl + API_PATH, new TranslateQuery(source, target, word), TranslatedText.class);
        return Objects.requireNonNull(response.getBody()).getTranslatedText();
    }

    public String translate(final String source, final String target, final String query) {
        final Stream<String> words = BreakWords.breakWords(query, Locale.of(source));
        try {
            return executorService.invokeAll(words
                    .map(word -> (Callable<String>)
                            () -> BreakWords.isWord(word) ? translateWord(source, target, word) : word)
                            .toList()).stream()
                    .map(future -> {
                try {
                    return future.get();
                } catch (final ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.joining());
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
