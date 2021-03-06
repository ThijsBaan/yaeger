package nl.han.ica.yaeger.engine.entities.entity.sprites;

import com.google.inject.Injector;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.han.ica.yaeger.engine.entities.entity.Position;
import nl.han.ica.yaeger.engine.entities.entity.sprites.delegates.SpriteAnimationDelegate;
import nl.han.ica.yaeger.engine.entities.events.RemoveEntityEvent;
import nl.han.ica.yaeger.engine.media.repositories.ImageRepository;
import nl.han.ica.yaeger.javafx.factories.image.ImageViewFactory;
import nl.han.ica.yaeger.module.factories.SpriteAnimationDelegateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class SpriteEntityTest {

    private final static String DEFAULT_RESOURCE = "images/bubble.png";
    private final static int X_POSITION = 37;
    private final static int Y_POSITION = 37;
    private final static Position DEFAULT_POSITION = new Position(X_POSITION, Y_POSITION);
    private final static int WIDTH = 39;
    private final static int HEIGHT = 41;
    private final static Size DEFAULT_SIZE = new Size(WIDTH, HEIGHT);

    private SpriteAnimationDelegateFactory spriteAnimationDelegateFactory;
    private ImageViewFactory imageViewFactory;
    private SpriteAnimationDelegate spriteAnimationDelegate;
    private ImageRepository imageRepository;
    private Injector injector;

    @BeforeEach
    void setup() {
        imageViewFactory = mock(ImageViewFactory.class);
        spriteAnimationDelegate = mock(SpriteAnimationDelegate.class);
        spriteAnimationDelegateFactory = mock(SpriteAnimationDelegateFactory.class);
        when(spriteAnimationDelegateFactory.create(any(ImageView.class), anyInt())).thenReturn(spriteAnimationDelegate);

        imageRepository = mock(ImageRepository.class);
        injector = mock(Injector.class);
    }

    @Test
    void instantiatingASpriteEntityWithOneFrameGivesNoSideEffects() {
        // Setup

        // Test
        var spriteEntity = new TestSpriteEntityWithDefaultFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE);

        // Verify
        Assertions.assertNotNull(spriteEntity);
    }

    @Test
    void instantiatingASpriteEntityWithTwoFrameGivesNoSideEffects() {
        // Setup

        // Test
        var spriteEntity = new TestSpriteEntityWithTwoFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE, 2);

        // Verify
        Assertions.assertNotNull(spriteEntity);
    }

    @Test
    void callingInitAfterInstantiatingWithSingleFrameImageWiresDelegates() {
        // Setup
        var spriteEntity = new TestSpriteEntityWithDefaultFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE);
        spriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        spriteEntity.setImageRepository(imageRepository);
        spriteEntity.setImageViewFactory(imageViewFactory);

        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);

        // Test
        spriteEntity.init(injector);

        // Verify
        verifyNoMoreInteractions(spriteAnimationDelegateFactory);
    }

    @Test
    void callingInitAfterInstantiatingWithDoubleFrameImageWiresDelegates() {
        // Setup
        var spriteEntity = new TestSpriteEntityWithTwoFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE, 2);
        spriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        spriteEntity.setImageRepository(imageRepository);
        spriteEntity.setImageViewFactory(imageViewFactory);

        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH * 2, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);

        // Test
        spriteEntity.init(injector);

        // Verify
        verify(spriteAnimationDelegateFactory).create(imageView, 2);
    }

    @Test
    void getXReturnsTheExpectedXCoordinate() {
        var spriteEntity = new TestSpriteEntityWithDefaultFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE);
        spriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        spriteEntity.setImageRepository(imageRepository);
        spriteEntity.setImageViewFactory(imageViewFactory);

        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);
        spriteEntity.init(injector);

        when(imageView.getX()).thenReturn(DEFAULT_POSITION.getX());
        when(imageView.getY()).thenReturn(DEFAULT_POSITION.getY());

        // Test
        var x = spriteEntity.getXPosition();

        // Verify
        Assertions.assertEquals(X_POSITION, x);
    }

    @Test
    void getYReturnsTheExpectedXCoordinate() {
        var spriteEntity = new TestSpriteEntityWithDefaultFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE);
        spriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        spriteEntity.setImageRepository(imageRepository);
        spriteEntity.setImageViewFactory(imageViewFactory);

        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);
        spriteEntity.init(injector);

        when(imageView.getX()).thenReturn(DEFAULT_POSITION.getX());
        when(imageView.getY()).thenReturn(DEFAULT_POSITION.getY());

        // Test
        var y = spriteEntity.getYPosition();

        // Verify
        Assertions.assertEquals(Y_POSITION, y);
    }

    @Test
    void removingAnEntitySetsImageViewCorrectly() {
        // Setup
        var spriteEntity = new TestSpriteEntityWithDefaultFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE);
        spriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        spriteEntity.setImageRepository(imageRepository);
        spriteEntity.setImageViewFactory(imageViewFactory);

        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);
        spriteEntity.init(injector);

        // Test
        spriteEntity.remove();

        // Verify
        verify(imageView).setImage(null);
        verify(imageView).setVisible(false);
        verify(imageView).fireEvent(any(RemoveEntityEvent.class));
    }

    @Test
    void getPositionReturnsCorrectPosition() {
        // Setup
        var spriteEntity = new TestSpriteEntityWithDefaultFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE);
        spriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        spriteEntity.setImageRepository(imageRepository);
        spriteEntity.setImageViewFactory(imageViewFactory);

        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);
        spriteEntity.init(injector);

        when(imageView.getX()).thenReturn(DEFAULT_POSITION.getX());
        when(imageView.getY()).thenReturn(DEFAULT_POSITION.getY());

        // Test
        var position = spriteEntity.getPosition();

        // Verify
        Assertions.assertEquals(DEFAULT_POSITION, position);
    }

    @Test
    void setPositionDelegatesToTheImageView() {
        // Setup
        var spriteEntity = new TestSpriteEntityWithDefaultFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE);
        spriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        spriteEntity.setImageRepository(imageRepository);
        spriteEntity.setImageViewFactory(imageViewFactory);

        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);
        spriteEntity.init(injector);

        // Test
        var position = new Position(42, 48);
        spriteEntity.setPosition(position);

        // Verify
        verify(imageView).setX(position.getX());
        verify(imageView).setY(position.getY());
    }

    @Test
    void setRotationDelegatesToTheImageView() {
        // Setup
        var spriteEntity = new TestSpriteEntityWithDefaultFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE);
        spriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        spriteEntity.setImageRepository(imageRepository);
        spriteEntity.setImageViewFactory(imageViewFactory);

        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);
        spriteEntity.init(injector);

        // Test
        var rotation = 45d;
        spriteEntity.rotate(rotation);

        // Verify
        verify(imageView).setRotate(rotation);
    }

    @Test
    void setFrameIndexDelegatesToSpriteAnimationDelegate() {
        // Setup
        var spriteEntity = new TestSpriteEntityWithTwoFrames(DEFAULT_RESOURCE, DEFAULT_POSITION, DEFAULT_SIZE, 2);
        spriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        spriteEntity.setImageRepository(imageRepository);
        spriteEntity.setImageViewFactory(imageViewFactory);

        var frames = 2;
        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH * frames, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);


        var spriteAnimationDelegate = mock(SpriteAnimationDelegate.class);
        when(spriteAnimationDelegateFactory.create(imageView, 2)).thenReturn(spriteAnimationDelegate);

        spriteEntity.init(injector);

        // Test
        spriteEntity.setCurrentFrameIndex(frames);

        // Verify
        verify(spriteAnimationDelegate).setSpriteIndex(frames);
    }

    private class TestSpriteEntityWithDefaultFrames extends SpriteEntity {

        TestSpriteEntityWithDefaultFrames(String resource, Position position, Size size) {
            super(resource, position, size);
        }

        double getXPosition() {
            return getX();
        }

        double getYPosition() {
            return getY();
        }
    }

    private class TestSpriteEntityWithTwoFrames extends SpriteEntity {

        TestSpriteEntityWithTwoFrames(String resource, Position position, Size size, int frames) {
            super(resource, position, size, frames);
        }
    }
}
