package view;

import adapters.controller.MenuActions;
import adapters.viewmodel.LobbyViewModel;
import adapters.viewmodel.Screen;
import adapters.viewmodel.ViewState;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.Map;

/** Handles local navigation and displays screens selected by the application. */
public final class ViewManager extends JPanel implements PropertyChangeListener {
    private final CardLayout layout = new CardLayout();
    private final Map<Screen, ArtworkMenu> menus = new EnumMap<>(Screen.class);
    private final ViewState<Screen> screenState;
    private final ViewState<LobbyViewModel> lobbyState;
    private final HostScreen host;
    private final WaitingToStartScreen waiting;
    private Screen visibleScreen;
    private String nickname = "";

    public ViewManager(ViewState<Screen> screenState, ViewState<LobbyViewModel> lobbyState,
                       GameView game, MenuActions actions) {
        this.screenState = screenState;
        this.lobbyState = lobbyState;
        setLayout(layout);

        host = new HostScreen(() -> leaveLobby(actions));
        JoinScreen join = new JoinScreen(code -> actions.joinGame(nickname, code), () -> leaveLobby(actions));
        waiting = new WaitingToStartScreen(() -> leaveLobby(actions), actions::startGame);

        addMenu(Screen.TITLE, new TitleScreen(() -> showScreen(Screen.NAME), actions::showRules, actions::quit));
        addMenu(Screen.NAME, new NameScreen(name -> {
            nickname = name;
            showScreen(Screen.PLAY);
        }, () -> showScreen(Screen.TITLE)));
        addMenu(Screen.PLAY, new PlayScreen(() -> {
            showScreen(Screen.HOST);
            actions.hostGame(nickname);
        }, () -> {
            join.clearCode();
            showScreen(Screen.JOIN);
        }, () -> actions.loadSave(nickname), () -> showScreen(Screen.TITLE)));
        addMenu(Screen.HOST, host);
        addMenu(Screen.JOIN, join);
        addMenu(Screen.WAITING_TO_START, waiting);
        add(game, Screen.GAME.name());

        screenState.addPropertyChangeListener(this);
        lobbyState.addPropertyChangeListener(this);
        renderLobby();
        displayScreen();
    }

    /**
     * Publishes a screen change. After confirming another player is in the lobby, publish
     * WAITING_TO_START. Publish GAME after the host's start request is accepted.
     */
    public void showScreen(Screen screen) {
        screenState.setState(screen);
    }

    /** Shows an inline error on the named menu. Pass an empty message to clear it. */
    public void showMenuError(Screen screen, String message) {
        ArtworkMenu menu = menus.get(screen);
        if (menu == null) {
            throw new IllegalArgumentException("No menu error area for " + screen);
        }
        onEventThread(() -> menu.showError(message));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getSource() == screenState) {
            onEventThread(this::displayScreen);
        } else if (event.getSource() == lobbyState) {
            onEventThread(this::renderLobby);
        }
    }

    private void addMenu(Screen screen, ArtworkMenu menu) {
        menus.put(screen, menu);
        add(menu, screen.name());
    }

    private void displayScreen() {
        Screen next = screenState.getState();
        if (visibleScreen != next) {
            ArtworkMenu previous = menus.get(visibleScreen);
            if (previous != null) {
                previous.showError("");
            }
            layout.show(this, next.name());
            visibleScreen = next;
        }
    }

    private void renderLobby() {
        LobbyViewModel lobby = lobbyState.getState();
        host.showCode(lobby.code());
        waiting.render(lobby);
    }

    private void leaveLobby(MenuActions actions) {
        showScreen(Screen.PLAY);
        actions.leaveLobby();
    }

    private static void onEventThread(Runnable update) {
        if (SwingUtilities.isEventDispatchThread()) {
            update.run();
        } else {
            SwingUtilities.invokeLater(update);
        }
    }
}
