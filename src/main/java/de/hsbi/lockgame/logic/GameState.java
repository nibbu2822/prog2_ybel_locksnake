package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.model.*;

import java.util.ArrayList;
import java.util.List;

public final class GameState {
    private Level level;
    private Snake snake;
    private List<Pin> pins;
    private Status status;
    private Direction direction;

  public GameState(
      Level level, Snake snake, List<Pin> pins, Status status, Direction pendingDirection) {
    // TODO: lege einen neuen GameState mit den übergebenen Informationen an
    //throw new UnsupportedOperationException("method not implemented yet");
      this.level = level;
      this.snake = snake;
      this.pins = pins;
      this.status = status;
      this.direction = pendingDirection;
  }

  public Level level() {
    // TODO: Getter
    //throw new UnsupportedOperationException("method not implemented yet");
      return level;
  }

  public Snake snake() {
    // TODO: Getter
    //throw new UnsupportedOperationException("method not implemented yet");
      return snake;
  }

  public List<Pin> pins() {
    // TODO: Getter
    //throw new UnsupportedOperationException("method not implemented yet");
      return pins;
  }

  public Status status() {
    // TODO: Getter
    //throw new UnsupportedOperationException("method not implemented yet");
    return status;
  }

  public Direction pendingDirection() {
    // TODO: Getter
    //throw new UnsupportedOperationException("method not implemented yet");
      return direction;
  }

  public GameState tick() {
    // TODO: diese Methode lässt das Spiel einen Schritt laufen (berechnet den Spielzustand im
    // nächsten Schritt)

      if (status == Status.RUNNING) {
          // TODO: early exit: wenn das Spiel nicht läuft oder keine Blickrichtung gesetzt ist: keine
          // Änderung
          if (direction == Direction.NONE || !status.isRunning()) {
              return this;
          }


          Status newStatus = status;
          // TODO: prüfe die folgenden Bedingungen:
          // (a) Schlange würde das Spielfeld verlassen: Spiel verloren
          Position current = snake.nextHead(direction);
          if (current.x() >= level.width() ||
              current.x() < 0 ||
              current.y() >= level.height() ||
              current.y() < 0) {
              newStatus = Status.LOST_OUT_OF_BOUNDS;
              return new GameState(level, snake, pins, newStatus, direction);
          }
          // (b) Schlange würde in ein Wandelement gehen: Blockiert (keine Bewegung, Blickrichtung "none")
          if (level.cellAt(current).equals(CellType.WALL)) {
                direction = Direction.NONE;
              return new GameState(level, snake, pins, newStatus, direction);
          }
          // (c) Schlange beisst sich: Spiel verloren
          if (snake.occupies(current)) {
              newStatus = Status.LOST_SELF_COLLISION;
              return new GameState(level, snake, pins, newStatus, direction);
          }
          // (d) Schlange würde auf einen Pin gehen (Pin bereits gesetzt oder Schlange kommt nicht in der
          // Aktivierungsrichtung): Blockiert (keine Bewegung, Blickrichtung "none")
          if (level.cellAt(current).equals(CellType.PIN_SLOT)) {
              List<Pin> newPins = new ArrayList<>(pins);
              Pin matchingPin = pins.stream().filter(pin -> pin.position().equals(current)).findFirst().orElse(null);
              if(matchingPin.activationDirection().equals(direction)) {
                  newPins.remove(matchingPin);
                  newPins.add(matchingPin.withState(Pin.State.HIGH));
              }
              direction = Direction.NONE;
              if (newPins.stream().allMatch(pin -> pin.state() == Pin.State.HIGH)) {
                  newStatus = Status.WON;
              }

              return new GameState(level, snake, newPins, newStatus, direction);
          }
    // TODO: anderenfalls: bewege die Schlange um einen Schritt in Blickrichtung (falls gesetzt)
          snake = snake.grow(direction);
      }
      return new GameState(level, snake, pins, status, direction);
      //throw new UnsupportedOperationException("method not implemented yet");
  }

  public enum Status {
    RUNNING,
    WON,
    LOST_SELF_COLLISION,
    LOST_OUT_OF_BOUNDS;

    public boolean isRunning() {
      return this == RUNNING;
    }
  }
}
