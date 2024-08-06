package nbogdan.translator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nbogdan.translator.api.TranslateException;
import nbogdan.translator.api.dto.Language;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class TranslatorController {
    private final Language[] languages;
    private final TranslateService service;
    private final JdbcTemplate jdbcTemplate;

    public TranslatorController(final Language[] languages,
                                final TranslateService service,
                                final JdbcTemplate jdbcTemplate) {
        this.languages = languages;
        this.service = service;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/")
    public String index(final Model model,
                        @ModelAttribute final Translation translation,
                        final HttpServletRequest request,
                        final HttpServletResponse response) {
        model.addAttribute("translation", translation);
        model.addAttribute("languages", languages);
        final String query = translation.getQuery();
        final String source = translation.getSource();
        final String target = translation.getTarget();
        if (query != null && source != null && target != null) {
            String result;
            boolean successful;
            try {
                result = service.translate(source, target, query);
                model.addAttribute("translated", result);
                successful = true;
            } catch (final TranslateException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                model.addAttribute("error", e.getMessage());
                result = e.getMessage();
                successful = false;
            }
            writeToDatabase(request.getRemoteAddr(), query, result, successful);
        }
        return "index";
    }

    private void writeToDatabase(
            final String address, final String query, final String result, final boolean successful) {
        jdbcTemplate.update("INSERT INTO requests(ip, query, result, successful) VALUES (?::inet, ?, ?, ?)",
                address, query, result, successful);
    }
}
