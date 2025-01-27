package cool.o11y.clusternamer.internal;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import cool.o11y.clusternamer.NamingTheme;

public record NamingSuggestionChunk(
        @JsonUnwrapped NamingTheme theme,
        Collection<String> names) {

    @JsonIgnore
    public static Collection<String> sanitizeNames(Collection<String> names) {
        return names.stream()
                .map(n -> n.toLowerCase())
                .map(n -> n.replace("ä", "ae"))
                .map(n -> n.replace("ö", "oe"))
                .map(n -> n.replace("ü", "ue"))
                .map(n -> n.replace("ß", "ss"))
                .map(n -> n.replace("&", "and"))
                .map(n -> asciiFold(n).replaceAll("\s+", ""))
                .collect(Collectors.toSet());
    }

    private static String asciiFold(String in) {
        final char[] input = in.toCharArray();
        final char[] output = new char[input.length];

        ASCIIFoldingFilter.foldToASCII(input, 0, output, 0, input.length);

        return new String(output);
    }

}
