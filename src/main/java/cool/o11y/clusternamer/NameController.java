package cool.o11y.clusternamer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cool.o11y.clusternamer.NamingSuggestions.NamingSuggestion;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class NameController {

    private final ClusterNameService nameService;

    @GetMapping("/next")
    public NamingSuggestion nextName() {
        return nameService.next();
    }

    @GetMapping("/")
    public NamingSuggestion currentName() {
        return nameService.current();
    }


}
