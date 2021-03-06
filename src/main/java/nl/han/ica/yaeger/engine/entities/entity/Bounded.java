package nl.han.ica.yaeger.engine.entities.entity;

import javafx.geometry.Bounds;

/**
 * Implementing this interface exposes the {@code getBounds} method, which returns the bounds, aka
 * Bounding Box, of this Entity.
 */
public interface Bounded {

    /**
     * Return the Bounds, aka Bounding Box, of this Entity.
     *
     * @return The Bounds of this Entity.
     */
    Bounds getBounds();
}
