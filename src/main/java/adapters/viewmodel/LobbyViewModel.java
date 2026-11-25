package adapters.viewmodel;

import java.util.List;
import java.util.Objects;

/** The lobby code, roster in display order, and whether the local player may start. */
public record LobbyViewModel(String code, List<String> players, boolean canStart) {
    public LobbyViewModel {
        Objects.requireNonNull(code);
        players = List.copyOf(players);
    }

    public static LobbyViewModel empty() {
        return new LobbyViewModel("", List.of(), false);
    }
}
