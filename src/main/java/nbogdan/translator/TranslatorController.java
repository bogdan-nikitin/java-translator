package nbogdan.translator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nbogdan.translator.api.TranslateException;
import nbogdan.translator.api.dto.Language;
import nbogdan.translator.api.dto.Translation;
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
        if (query != null && translation.getSource() != null && translation.getTarget() != null) {
            try {
                final String translated = service.translate(translation.getSource(), translation.getTarget(), translation.getQuery());
                model.addAttribute("translated", translated);
                jdbcTemplate.update("INSERT INTO requests(ip, query, result) VALUES (?::inet, ?, ?)",
                        request.getRemoteAddr(), query, translated);
            } catch (final TranslateException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                model.addAttribute("error", e.getMessage());
            }
        }
        return "index";
    }
}
