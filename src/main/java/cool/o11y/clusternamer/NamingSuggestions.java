package cool.o11y.clusternamer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import cool.o11y.clusternamer.internal.NamingSuggestionChunk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NamingSuggestions {

    public record NamingSuggestion(@JsonUnwrapped NamingTheme theme, String suggestion) {
    }

    @Getter
    private final NamingTheme theme;

    @Getter
    private final List<String> usedSuggestions = new ArrayList<>();
    private final Queue<String> nextSuggestions = new ArrayDeque<>();

    public void add(NamingSuggestionChunk chunk) {
        Objects.equals(theme, chunk.theme());

        // remove all already used suggestions from incoming suggestions
        Collection<String> suggestions = chunk.names();
        suggestions.removeAll(usedSuggestions);

        nextSuggestions.addAll(suggestions);
    }

    public NamingSuggestion current() {
        Assert.isTrue(!usedSuggestions.isEmpty(), "No current suggestion active");

        return new NamingSuggestion(theme, usedSuggestions.getLast());
    }

    public boolean hasNext() {
        return !nextSuggestions.isEmpty();
    }

    public NamingSuggestion next() {
        Assert.isTrue(hasNext(), "No suggestion available");

        // rotate suggestion
        usedSuggestions.add(nextSuggestions.remove());

        return current();
    }
}
