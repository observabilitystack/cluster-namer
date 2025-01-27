package cool.o11y.clusternamer;

import org.springframework.stereotype.Service;

import cool.o11y.clusternamer.NamingSuggestions.NamingSuggestion;
import cool.o11y.clusternamer.internal.ClusterNameRepository;
import cool.o11y.clusternamer.internal.ClusterThemeRepository;
import cool.o11y.clusternamer.internal.NamingSuggestionChunk;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClusterNameService {

    private final ClusterNameRepository nameRepository;
    private final ClusterThemeRepository themeRepository;

    // the state of naming suggestions
    private NamingSuggestions suggestions;

    public NamingSuggestion current() {
        return suggestions.current();
    }

    public NamingSuggestion next() {
        if (!suggestions.hasNext()) {
            final NamingSuggestionChunk chunk = nameRepository.nextChunk(themeRepository.currentTheme(), suggestions);
            suggestions.add(chunk);
        }

        return suggestions.next();
    }

    public NamingTheme setCurrentTheme(NamingTheme theme) {
        themeRepository.setCurrentTheme(theme);

        // update internal state
        this.suggestions = new NamingSuggestions(themeRepository.currentTheme());

        return suggestions.getTheme();
    }

    public NamingTheme currentTheme() {
        return suggestions.getTheme();
    }

    @PostConstruct
    public void init() {

        // init internal state
        this.suggestions = new NamingSuggestions(themeRepository.currentTheme());
    }

}
