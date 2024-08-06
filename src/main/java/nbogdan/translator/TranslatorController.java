package nbogdan.translator;

import jakarta.servlet.http.HttpServletRequest;
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
                        final HttpServletRequest request) {
        model.addAttribute("translation", translation);
        model.addAttribute("languages", languages);
        final String query = translation.getQuery();
        if (query != null && translation.getSource() != null && translation.getTarget() != null) {
            final String translated = service.translate(translation.getSource(), translation.getTarget(), translation.getQuery());
            model.addAttribute("translated", translated);
            jdbcTemplate.update("INSERT INTO requests(ip, query, result) VALUES (?::inet, ?, ?)",
                    request.getRemoteAddr(), query, translated);
        }
        return "index";
    }
}
