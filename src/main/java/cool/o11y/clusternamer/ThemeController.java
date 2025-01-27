package cool.o11y.clusternamer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ThemeController {

    private final ClusterNameService nameService;

    @PutMapping(path = "/theme")
    @ResponseStatus(code = HttpStatus.CREATED)
    public NamingTheme setTheme(@RequestParam() NamingTheme theme) {
        return nameService.setCurrentTheme(theme);
    }

    @GetMapping(path = "/theme")
    public NamingTheme getTheme() {
        return nameService.currentTheme();
    }

}
