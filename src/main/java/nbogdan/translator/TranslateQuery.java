package nbogdan.translator;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TranslateQuery {
    private String source;
    private String target;
    private String q;
}
