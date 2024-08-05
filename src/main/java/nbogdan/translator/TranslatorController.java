package nbogdan.translator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class TranslatorController {
    private final Language[] languages;

    public TranslatorController(final Language[] languages) {
        this.languages = languages;
    }

    @GetMapping("/")
    public String index(final Model model, @ModelAttribute Translation translation) {
        model.addAttribute("translation", translation);
        model.addAttribute("languages", languages);
        return "index";
    }
}
