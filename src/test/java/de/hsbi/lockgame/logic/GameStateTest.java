package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.io.LevelLoader;
import de.hsbi.lockgame.logic.GameState;
import de.hsbi.lockgame.logic.GameState.Status;
import de.hsbi.lockgame.model.*;
import de.hsbi.lockgame.settings.LevelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private GameState initialState;
    private Level level;

    @BeforeEach
    void setup() {
        try {
            level = LevelLoader.loadLevelFromResource(LevelConstants.defaultLevel());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Fehler beim Starten des Spiels: " + e.getMessage(),
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void init() {
        this.init(Direction.NONE);
    }

    private void init(Direction direction) {
        this.init(direction, level.snakeStart());
    }

    private void init(Direction direction, Position start) {
        this.init(direction, start, level.pins());
    }

    public void init(Direction direction, Position start, List<Pin> pins) {
        List<Position> body = new ArrayList<>();
        body.add(start);
        Snake snake = new Snake(body);
        this.init(direction, start, pins, snake);
    }

    private void init(Direction direction, Position start, List<Pin> pins, Snake snake) {
        initialState = new GameState(level, snake, pins, Status.RUNNING, direction);
    }

    // -------------------------
    // 1. INITIAL STATE
    // -------------------------
    @Test
    void initialState() {
        init();
        assertTrue(initialState.status().isRunning());
        assertEquals(initialState.snake().head(), level.snakeStart());
        assertEquals(initialState.pendingDirection(), Direction.NONE);
        assertEquals(initialState.pins(), level.pins());
    }

    // -------------------------
    // 2. Snake Move Left
    // -------------------------
    @Test
    void snake_move_left() {
        init(Direction.LEFT);
        initialState = initialState.tick();
        Position target = new Position(level.snakeStart().x()-1, level.snakeStart().y());
        assertEquals(initialState.snake().head(), target);
    }

    // -------------------------
    // 3. Snake Move Multiple Left
    // -------------------------
    @Test
    void snake_move_multiple_left() {
        init(Direction.LEFT);
        initialState = initialState.tick();
        initialState = initialState.tick();
        Position target = new Position(level.snakeStart().x()-2, level.snakeStart().y());
        assertEquals(initialState.snake().head(), target);
    }

    // -------------------------
    // 4. Snake Move Right
    // -------------------------
    @Test
    void snake_move_right() {
        Position start = new Position(level.snakeStart().x()-2, level.snakeStart().y());
        init(Direction.RIGHT, start);
        initialState = initialState.tick();
        Position target = new Position(level.snakeStart().x()-1, level.snakeStart().y());
        assertEquals(initialState.snake().head(), target);
    }

    // -------------------------
    // 5. Snake Moves Multiple Right
    // -------------------------
    @Test
    void snake_move_multiple_right() {
        Position start = new Position(level.snakeStart().x()-2, level.snakeStart().y());
        init(Direction.RIGHT, start);
        initialState = initialState.tick();
        initialState = initialState.tick();
        Position target = new Position(level.snakeStart().x(), level.snakeStart().y());
        assertEquals(initialState.snake().head(), target);
    }

    // -------------------------
    // 6. Snake Move Up
    // -------------------------
    @Test
    void snake_move_Up() {
        init(Direction.UP);
        initialState = initialState.tick();
        Position target = new Position(level.snakeStart().x(), level.snakeStart().y() - 1);
        assertEquals(initialState.snake().head(), target);
    }

    // -------------------------
    // 7. Snake Move Multiple Up
    // -------------------------
    @Test
    void snake_move_multiple_Up () {
        init(Direction.UP);
        initialState = initialState.tick();
        initialState = initialState.tick();
        Position target = new Position(level.snakeStart().x(), level.snakeStart().y() - 2);
        assertEquals(initialState.snake().head(), target);
    }

    // -------------------------
    // 8. Snake Move Down
    // -------------------------
    @Test
    void snake_move_Down() {
        init(Direction.DOWN);
        initialState = initialState.tick();
        Position target = new Position(level.snakeStart().x(), level.snakeStart().y() +1);
        assertEquals(initialState.snake().head(), target);
    }

    // -------------------------
    // 9. Snake Moves Multiple Down
    // -------------------------
    @Test
    void snake_move_multiple_Down() {
        init(Direction.DOWN);
        initialState = initialState.tick();
        initialState = initialState.tick();
        Position target = new Position(level.snakeStart().x(), level.snakeStart().y()+2);
        assertEquals(initialState.snake().head(), target);
    }

    // -------------------------
    // 10. No Movement
    // -------------------------
    @Test
    void Movement_Direction_None_then_No_Movement() {
        init(Direction.NONE);
        initialState = initialState.tick();
        assertEquals(initialState.snake().head(), level.snakeStart());
        initialState = initialState.tick();
        assertEquals(initialState.snake().head(), level.snakeStart());
    }

    // -------------------------
    // 11. PIN ACTIVATION
    // -------------------------
    @Test
    void correct_Pin_and_Direction_then_activates() {
        Position start = new Position(level.snakeStart().x()-2, level.snakeStart().y());
        init(Direction.LEFT, start);
        for(Pin pin : initialState.pins()) {
            assertEquals(pin.state(), Pin.State.LOW);
        }
        initialState = initialState.tick();
        assertEquals(initialState.pins().size(), level.pins().size());
        for(int i = 0; i < initialState.pins().size(); i++) {
            if (i != 2) {
                assertEquals(initialState.pins().get(i).state(), Pin.State.LOW);
            } else {
                assertEquals(initialState.pins().get(i).state(), Pin.State.HIGH);
            }
        }
        assertEquals(initialState.status(), Status.RUNNING);
        assertEquals(initialState.snake().head(), start);
        assertEquals(initialState.pendingDirection(), Direction.NONE);
    }

    // -------------------------
    // 12. PIN WRONG DIRECTION
    // -------------------------
    @Test
    void wrong_Pin_direction_then_noActivation() {
        Position start = new Position(level.snakeStart().x()-4, level.snakeStart().y());
        init(Direction.RIGHT, start);
        for(Pin pin : initialState.pins()) {
            assertEquals(pin.state(), Pin.State.LOW);
        }
        initialState = initialState.tick();
        assertEquals(initialState.pins().size(), level.pins().size());
        for(int i = 0; i < initialState.pins().size(); i++) {
            System.out.println("index "+i+" value" + initialState.pins().get(i).state());
            assertEquals(initialState.pins().get(i).state(), Pin.State.LOW);
        }
        assertEquals(initialState.status(), Status.RUNNING);
        assertEquals(initialState.snake().head(), start);
        assertEquals(initialState.pendingDirection(), Direction.NONE);
    }

    // -------------------------
    // 13. WIN CONDITION
    // -------------------------
    @Test
    void allPinsHigh_then_wins() {
        List<Pin> pins = new ArrayList<>();
        for(int i = 0; i < level.pins().size(); i++) {
            if(i != 2) {
                pins.add(i, level.pins().get(i).withState(Pin.State.HIGH));
            } else {
                pins.add(i, level.pins().get(i).withState(Pin.State.LOW));
            }
        }
        Position start = new Position(level.snakeStart().x()-2, level.snakeStart().y());
        init(Direction.LEFT, start, pins);
        assertEquals(initialState.pins().size(), level.pins().size());
        for(int i = 0; i < initialState.pins().size(); i++) {
            if(i != 2) {
                assertEquals(initialState.pins().get(i).state(), Pin.State.HIGH);
            }
        }
        initialState = initialState.tick();
        assertEquals(initialState.status(), Status.WON);
    }

    // -------------------------
    // 14. Wall Collision
    // -------------------------
    @Test
    void Collision_to_wall_then_No_Movement_and_State_Running() {
        init(Direction.RIGHT);
        initialState = initialState.tick();
        assertEquals(initialState.status(), Status.RUNNING);
        assertEquals(initialState.snake().head(), level.snakeStart());
        assertEquals(initialState.pendingDirection(), Direction.NONE);
    }

    // -------------------------
    // 15. STATE Eat Self Lose
    // -------------------------
    @Test
    void Eat_Self_Collision_then_Lose() {
        Position start = new Position(level.snakeStart().x()-1, level.snakeStart().y());
        List<Position> body = new ArrayList<>();
        body.add(start);
        Snake snake = new Snake(body);
        snake = snake.grow(Direction.LEFT);
        init(Direction.RIGHT, start, level.pins(), snake);
        initialState = initialState.tick();
        assertEquals(initialState.status(), Status.LOST_SELF_COLLISION);
    }

    // -------------------------
    // 16. Out of Bounds
    // -------------------------
    @Test
    void Going_Out_Of_Bounds_then_Lose() {
        Position start = new Position(level.snakeStart().x()+1, level.snakeStart().y());
        init(Direction.RIGHT, start);
        initialState = initialState.tick();
        assertEquals(initialState.status(), Status.LOST_OUT_OF_BOUNDS);
    }
}
