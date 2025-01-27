package cool.o11y.clusternamer.internal;

import org.springframework.stereotype.Repository;

import cool.o11y.clusternamer.NamingTheme;

@Repository
public class ClusterThemeRepository {

    private NamingTheme current = new NamingTheme("Mc Donald's burgers");

    public NamingTheme currentTheme() {
        return current;
    }

    public void setCurrentTheme(NamingTheme theme) {
        this.current = theme;
    }

}
