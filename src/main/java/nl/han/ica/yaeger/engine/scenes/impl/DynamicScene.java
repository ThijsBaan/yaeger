package nl.han.ica.yaeger.engine.scenes.impl;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import nl.han.ica.yaeger.engine.entities.entity.Entity;
import nl.han.ica.yaeger.engine.entities.EntitySpawner;

import java.util.Set;

/**
 * Instantiate a new  {@code DynamicScene}. A {@code DynamicScene} extends a {@link StaticScene}, but adds its
 * own {@code Gameloop}.
 */
public abstract class DynamicScene extends StaticScene {

    private AnimationTimer animator;

    @Override
    public void configure() {
        super.configure();

        createGameLoop();

        setupSpawners();

        startGameLoop();
    }

    protected abstract void setupSpawners();

    @Override
    public void onInputChanged(Set<KeyCode> input) {
        entityCollection.notifyGameObjectsOfPressedKeys(input);
    }

    /**
     * Register an {@link EntitySpawner}. After being registered, the {@link EntitySpawner} will be responsible for spawning
     * new instances of {@link Entity}.
     *
     * @param spawner the {@link EntitySpawner} to be registered
     */
    protected void registerSpawner(EntitySpawner spawner) {
        entityCollection.registerSupplier(spawner);
    }

    @Override
    public void destroy() {
        stopGameLoop();
        entitySupplier.clear();
        entityCollection.clear();

        super.destroy();
    }

    private void startGameLoop() {
        animator.start();
    }

    private void stopGameLoop() {
        animator.stop();
        animator = null;
    }

    private void createGameLoop() {
        animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {

                entityCollection.update(arg0);
            }
        };
    }
}
