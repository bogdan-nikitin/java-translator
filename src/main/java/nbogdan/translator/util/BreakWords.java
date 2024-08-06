package nbogdan.translator.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.stream.Stream;

public class BreakWords {
    @AllArgsConstructor
    @Data
    private static class Slice {
        private int start;
        private int end;
    }

    public static Stream<String> breakWords(final String text, final Locale locale) {
        final BreakIterator boundary = BreakIterator.getWordInstance(locale);
        boundary.setText(text);

        return Stream.iterate(
                new Slice(boundary.first(), boundary.next()),
                slice -> slice.getEnd() != BreakIterator.DONE,
                slice -> {
                    slice.setStart(slice.getEnd());
                    slice.setEnd(boundary.next());
                    return slice;
                }).map(slice -> text.substring(slice.getStart(), slice.getEnd()));
    }

    public static boolean isWord(final String string) {
        return string.chars().anyMatch(Character::isLetter);
    }
}
