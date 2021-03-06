package nl.han.ica.waterworld.scenes;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.ica.waterworld.Waterworld;
import nl.han.ica.yaeger.engine.entities.entity.Position;
import nl.han.ica.yaeger.engine.entities.entity.text.TextEntity;
import nl.han.ica.yaeger.engine.scenes.SceneType;
import nl.han.ica.yaeger.engine.scenes.impl.StaticScene;

import java.util.Set;

public class GameOver extends StaticScene {

    public static final String GAME_OVER = "Game over";
    public static final String BACKGROUND_IMAGE = "underwater2.jpg";
    public static final String BACKGROUND_AUDIO = "audio/ocean.mp3";

    private Waterworld waterworld;

    public GameOver(Waterworld waterworld) {
        this.waterworld = waterworld;
    }

    @Override
    public void onInputChanged(Set<KeyCode> input) {
        if (input.contains(KeyCode.ENTER)) {
            waterworld.nextScene(SceneType.INTRO);
        }
    }

    @Override
    public void setupScene() {
        setBackgroundImage(BACKGROUND_IMAGE);
        setBackgroundAudio(BACKGROUND_AUDIO);
    }

    @Override
    public void setupEntities() {
        var gameOverText = new TextEntity(new Position(440, 350), GAME_OVER);
        gameOverText.setFill(Color.VIOLET);
        gameOverText.setFont(Font.font(Waterworld.FONT, FontWeight.BOLD, 60));
        addEntity(gameOverText);
    }
}
