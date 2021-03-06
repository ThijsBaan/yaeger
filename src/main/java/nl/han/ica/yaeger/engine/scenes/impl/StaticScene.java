package nl.han.ica.yaeger.engine.scenes.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import nl.han.ica.yaeger.engine.entities.EntityCollection;
import nl.han.ica.yaeger.engine.entities.EntitySupplier;
import nl.han.ica.yaeger.engine.userinput.KeyListener;
import nl.han.ica.yaeger.engine.debug.Debugger;
import nl.han.ica.yaeger.engine.entities.entity.Entity;
import nl.han.ica.yaeger.engine.entities.EntitySpawner;
import nl.han.ica.yaeger.engine.scenes.YaegerScene;
import nl.han.ica.yaeger.engine.scenes.delegates.BackgroundDelegate;
import nl.han.ica.yaeger.engine.scenes.delegates.KeyListenerDelegate;
import nl.han.ica.yaeger.module.factories.EntityCollectionFactory;
import nl.han.ica.yaeger.module.factories.SceneFactory;

import java.util.Set;

public abstract class StaticScene implements YaegerScene, KeyListener {

    private EntityCollectionFactory entityCollectionFactory;
    private SceneFactory sceneFactory;

    protected Injector injector;

    protected EntityCollection entityCollection;
    EntitySupplier entitySupplier;
    private KeyListenerDelegate keyListenerDelegate;
    private BackgroundDelegate backgroundDelegate;

    private Scene scene;
    private Group root;
    Debugger debugger;

    @Override
    public void init(Injector injector) {
        this.injector = injector;
    }

    @Override
    public void configure() {
        scene = sceneFactory.create(root);

        entityCollection = entityCollectionFactory.create(root);
        entityCollection.init(injector);
        entityCollection.addStatisticsObserver(debugger);

        debugger.setup(root);
        keyListenerDelegate.setup(scene, this);
        backgroundDelegate.setup(scene);
    }

    @Override
    public void postActivation() {
        entityCollection.registerSupplier(entitySupplier);
        entityCollection.initialUpdate();
        debugger.toFront();
    }

    /**
     * Add an {@link Entity} to this {@link YaegerScene}. An {@link Entity} can only be added once.
     * <p>
     * This method can only be used to add an instance of {@link Entity} during initialisation.If
     * one should be added during the game, a {@link EntitySpawner} should be used.
     * </p>
     *
     * @param entity the {@link Entity} to be added
     */
    protected void addEntity(Entity entity) {
        entitySupplier.add(entity);
    }

    /**
     * Set the name of the background image file.
     *
     * @param url The name of the image file, including extention. Although many different file types are supported,
     *            the following types are preferred:
     *            <ul>
     *            <li>jpg, jpeg</li>
     *            <li>png</li>
     *            </ul>
     */
    protected void setBackgroundImage(String url) {
        backgroundDelegate.setBackgroundImage(url);
    }

    /**
     * Set the name of the background audio file. Currently only {@code *.mp3} files are supported.
     *
     * @param url The name of the audio file, including extention.
     */
    protected void setBackgroundAudio(String url) {
        backgroundDelegate.setBackgroundAudio(url);
    }

    /**
     * Implement this method to be informed when a key has been pressed or released.
     *
     * @param input A {@link Set} containing all keys currently pressed.
     */
    public abstract void onInputChanged(Set<KeyCode> input);

    @Override
    public Scene getScene() {
        return this.scene;
    }


    @Override
    public void destroy() {
        keyListenerDelegate.tearDown(scene);
        backgroundDelegate.destroy();
        clear();
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        if (input.contains(KeyCode.F1)) {
            debugger.toggle();
        }

        onInputChanged(input);
    }

    @Override
    public void clear() {
        root.getChildren().clear();
        root = null;
        scene = null;
    }

    /**
     * Set the {@link Group} to be used. The {@link Group} will be the root node of the graph that
     * will be constructed for this {@link Scene}.
     *
     * @param root the {@link Group} to be used
     */
    @Inject
    public void setRoot(Group root) {
        this.root = root;
    }

    /**
     * Set the {@link KeyListener} that should be used. In general, this will be the {@link YaegerScene}
     * itself.
     *
     * @param keyListenerDelegate the {@link KeyListener} to be used
     */
    @Inject
    public void setKeyListenerDelegate(KeyListenerDelegate keyListenerDelegate) {
        this.keyListenerDelegate = keyListenerDelegate;
    }

    /**
     * Set the {@link SceneFactory} that should be used to create a {@link Scene}.
     *
     * @param sceneFactory the {@link SceneFactory} to be used
     */
    @Inject
    public void setSceneFactory(SceneFactory sceneFactory) {
        this.sceneFactory = sceneFactory;
    }

    /**
     * Set the {@link Debugger} that should be used.
     *
     * @param debugger the {@link Debugger} to be used
     */
    @Inject
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    @Inject
    public void setBackgroundDelegate(BackgroundDelegate backgroundDelegate) {
        this.backgroundDelegate = backgroundDelegate;
    }

    @Inject
    public void setEntityCollectionFactory(EntityCollectionFactory entityCollectionFactory) {
        this.entityCollectionFactory = entityCollectionFactory;
    }

    @Inject
    public void setEntitySupplier(EntitySupplier entitySupplier) {
        this.entitySupplier = entitySupplier;
    }
}
