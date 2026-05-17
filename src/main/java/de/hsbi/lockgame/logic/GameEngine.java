package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.model.Direction;
import de.hsbi.lockgame.model.Level;
import de.hsbi.lockgame.model.Position;
import de.hsbi.lockgame.model.Snake;
import de.hsbi.lockgame.ui.GamePanel;

import java.util.ArrayList;
import java.util.List;

// TODO: Die GameEngine verwaltet den GameState.

// TODO: Die GameEngine wird durch den Timer im main() getriggert ("tick") und lässt den GameState
// daraufhin einen Schritt ausführen. Dann müssen alle für den GameState registrierten Observer
// benachrichtigt werden, damit das Spielfeld neu gezeichnet werden kann o.ä.

// TODO: Die GameEngine beobachtet die Tastatureingaben (gesetzt in GamePanel.setupKeyBindings()),
// die in Direction übersetzt und an GameEngine.update() übergeben werden. Wenn es eine neue Eingabe
// gibt, wird die "update"-Methode von GameEngine aufgerufen, und die GameEngine muss die
// Blickrichtung der Schlange aktualisieren und diese GameState-Änderung den für den GameState
// registrierten Observer mitteilen.

// TODO: Die GameEngine ist ein Observer für Direction: GameEngine.update(Direction)
// TODO: Die GameEngine ist ein Observable für GameState: GamePanel.update(GameState)
public final class GameEngine {

    private GameState gameState;
    private GamePanel gamePanel;

  public GameEngine(Level level) {
    // TODO: lege eine neue GameEngine mit den übergebenen Informationen an
    //throw new UnsupportedOperationException("method not implemented yet");
      List<Position> body = new ArrayList<>();
      body.add(level.snakeStart());
      Snake snake = new Snake(body);
      this.gameState = new GameState(level, snake, level.pins(), GameState.Status.RUNNING, Direction.NONE);
  }

  public GameState state() {
    // TODO: gebe den aktuellen Spielzustand zurück
    //throw new UnsupportedOperationException("method not implemented yet");
      return gameState;
  }

  public void setGamePanel(GamePanel panel) {
    // TODO: Setter
    //throw new UnsupportedOperationException("method not implemented yet");
    this.gamePanel = panel;
    panel.setGameEngine(this);
  }

  public void update(Direction d) {
    // TODO: aktualisiere den Blickwinkel der Schlange (GameState)
    // TODO: benachrichtige alle Observer und gibt den neuen Spielzustand mit (Neuzeichnen der
    // Spielfläche)
    //throw new UnsupportedOperationException("method not implemented yet");
    this.gameState = new GameState(gameState.level(), this.gameState.snake(), gameState.pins(), gameState.status(), d);
  }

  public void tick() {
    // TODO: lass das Spiel (den GameState) einen Schritt ("tick") machen
    // TODO: benachrichtige alle Observer und gibt den neuen Spielzustand mit (Neuzeichnen der
    // Spielfläche)
      this.gameState = this.gameState.tick();
      gamePanel.update(this.gameState);
    //throw new UnsupportedOperationException("method not implemented yet");
  }

}
