package nbogdan.translator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class TranslatorController {
    private final Language[] languages;
    private final TranslateService service;

    public TranslatorController(final Language[] languages, final TranslateService service) {
        this.languages = languages;
        this.service = service;
    }

    @GetMapping("/")
    public String index(final Model model, @ModelAttribute Translation translation) {
        model.addAttribute("translation", translation);
        model.addAttribute("languages", languages);
        if (translation.getQuery() != null && translation.getSource() != null && translation.getTarget() != null) {
            model.addAttribute("translated", service.translate(translation.getSource(), translation.getTarget(), translation.getQuery()));
        }
        return "index";
    }
}
