package nbogdan.translator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.stream.Stream;

public class BreakWords {
    private static final String WORD_BOUNDARY = "((?<=\\p{Alpha})(?=\\P{Alpha}))|((?<=\\P{Alpha})(?=\\p{Alpha}))";

    @AllArgsConstructor
    @Data
    private static class Slice {
        private int start;
        private int end;
    }

    public static Stream<String> breakWords(final String text, final Locale locale) {
        final BreakIterator boundary = BreakIterator.getWordInstance(locale);
        boundary.setText(text);

        return Stream.iterate(new Slice(boundary.first(), boundary.next()), slice -> slice.getEnd() != BreakIterator.DONE, slice -> {
            slice.setStart(slice.getEnd());
            slice.setEnd(boundary.next());
            return slice;
        }).map(slice -> text.substring(slice.getStart(), slice.getEnd()));
    }

    public static String[] breakWords(final String text) {
        return text.split(WORD_BOUNDARY);
    }
}
