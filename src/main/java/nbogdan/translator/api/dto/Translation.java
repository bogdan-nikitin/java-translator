package nbogdan.translator.api.dto;

import lombok.Data;

@Data
public class Translation {
    private String source;
    private String target;
    private String query;
}
