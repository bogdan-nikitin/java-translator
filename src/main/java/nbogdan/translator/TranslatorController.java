package nbogdan.translator;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class TranslatorController {
    private final RestTemplate restTemplate;
    private final Language[] languages;

    public TranslatorController(final RestTemplateBuilder builder, final Language[] languages) {
        this.restTemplate = builder.build();
        this.languages = languages;
    }

    @GetMapping("/")
    public String index(final Model model) {
        model.addAttribute("languages", languages);
        return "index";
    }
}
