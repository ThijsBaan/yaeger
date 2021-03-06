package nl.han.ica.waterworld.entities.game;

import nl.han.ica.waterworld.scenes.levels.Level;
import nl.han.ica.yaeger.engine.entities.collisions.Collider;
import nl.han.ica.yaeger.engine.entities.collisions.CollisionSide;
import nl.han.ica.yaeger.engine.entities.entity.Position;
import nl.han.ica.yaeger.engine.entities.entity.sprites.Size;
import nl.han.ica.yaeger.engine.entities.entity.sprites.Movement;
import nl.han.ica.yaeger.engine.media.audio.SoundClip;
import nl.han.ica.yaeger.engine.scenes.SceneBorder;
import nl.han.ica.yaeger.engine.entities.collisions.Collided;
import nl.han.ica.yaeger.engine.entities.entity.sprites.UpdatableSpriteEntity;

public abstract class Bubble extends UpdatableSpriteEntity implements Collided {

    private static final String AUDIO_POP_MP3 = "audio/pop.mp3";
    private final Level level;

    Bubble(final Position position, final String resource, final double speed, final Level game) {
        super(resource, position, new Size(20, 20), 0, new Movement(Movement.Direction.UP, speed));
        this.level = game;
    }

    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        if (border.equals(SceneBorder.TOP)) {
            remove();
        }
    }

    @Override
    public void onCollision(Collider collidingObject, CollisionSide collisionSide) {
        if (collidingObject instanceof AnimatedShark) {
            handleSharkCollision();
        }
    }

    void handleSharkCollision() {
        removeBubble();
    }

    void handlePlayerCollision() {
        level.increaseBubblesPopped();

        removeBubble();
    }

    private void removeBubble() {
        SoundClip popSound = new SoundClip(AUDIO_POP_MP3);
        popSound.play();
        remove();
    }

    protected Level getLevel() {
        return level;
    }
}
