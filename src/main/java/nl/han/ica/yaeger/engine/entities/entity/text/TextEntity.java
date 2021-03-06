package nl.han.ica.yaeger.engine.entities.entity.text;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nl.han.ica.yaeger.engine.entities.entity.Entity;
import nl.han.ica.yaeger.engine.entities.entity.MouseButtonListener;
import nl.han.ica.yaeger.engine.entities.entity.Position;

/**
 * A {@code TextEntity} can be used to display a line of text on a {@link nl.han.ica.yaeger.engine.scenes.YaegerScene}.
 */
public class TextEntity implements Entity {

    private Text textDelegate;
    private Position position;
    private Color fill;
    private Font font;
    private String initialText;
    private boolean visible = true;

    /**
     * Instantiate a new {@code TextEntity}.
     */
    public TextEntity() {
        this(new Position(0, 0));
    }

    /**
     * Instantiate a new {@code TextEntity} for the given {@link Position}.
     *
     * @param position the initial {@link Position} of this {@code TextEntity}
     */
    public TextEntity(final Position position) {
        this(position, "");
    }

    /**
     * Instantiate a new {@code TextEntity} for the given {@link Position} and textDelegate.
     *
     * @param position the initial {@link Position} of this {@code TextEntity}
     * @param text     a {@link String} containing the initial textDelegate to be displayed
     */
    public TextEntity(final Position position, final String text) {
        this.position = position;
        this.initialText = text;
    }

    /**
     * Set the {@link String} that should be shown.
     *
     * @param text the {@link String} that should be shown
     */
    public void setText(final String text) {
        this.initialText = text;
        if (this.textDelegate != null) {
            this.textDelegate.setText(text);
        }
    }

    /**
     * Set the color of the textDelegate.
     *
     * @param color an instance of {@link Color}
     */
    public void setFill(Color color) {
        this.fill = color;
        if (textDelegate != null) {
            textDelegate.setFill(color);
        }
    }

    /**
     * Set the {@link Font} to be used. A {@link Font} encapsulates multiple properties.
     *
     * <p>
     * To only set the font type and size:
     * {@code setFont(Font.FONT ("Verdana", 20));}
     * <p>
     * It is also possible to set more properties:
     * {@code setFont(Font.FONT("Verdana", FontWeight.BOLD, 70));}
     *
     * @param font the {@link Font} to be used
     */
    public void setFont(Font font) {
        this.font = font;

        if (textDelegate != null) {
            textDelegate.setFont(font);
        }
    }

    /**
     * Set the {@link Position} of this {@code TextEntity}.
     *
     * @param position a {@link Position} encapsulating the x and y coordinate
     */
    public void setPosition(Position position) {
        this.position = position;

        if (textDelegate != null) {
            textDelegate.setX(position.getX());
            textDelegate.setY(position.getY());
        }
    }

    @Override
    public void remove() {
        textDelegate.setVisible(false);
        textDelegate.setText(null);
        notifyRemove();
    }

    @Override
    public Node getGameNode() {
        return textDelegate;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (textDelegate != null) {
            textDelegate.setVisible(visible);
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Inject
    public void setTextDelegate(Text text) {
        this.textDelegate = text;
    }

    @Override
    public void init(Injector injector) {
        if (position != null) {
            textDelegate.setX(position.getX());
            textDelegate.setY(position.getY());
        }
        if (font != null) {
            textDelegate.setFont(font);
        }
        if (fill != null) {
            textDelegate.setFill(fill);
        }
        if (initialText != null && !initialText.isEmpty()) {
            textDelegate.setText(initialText);
        }
        textDelegate.setVisible(visible);

        if (this instanceof MouseButtonListener) {
            ((MouseButtonListener) this).attachMousePressedListener();
        }
    }
}
