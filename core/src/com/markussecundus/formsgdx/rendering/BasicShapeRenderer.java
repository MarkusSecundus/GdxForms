package com.markussecundus.formsgdx.rendering;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Rozhraní, určené jako <code>Rend</code> parametr pro {@link com.markussecundus.forms.elements.Drawable}s,
 * která ke svému vykreslení potřebují pouze služby {@link ShapeRenderer}u.
 *
 *
 * @see BasicRenderer
 * @see ShapeRenderer
 * @see BatchRenderer
 *
 * @author MarkusSecundus
 * */
public interface BasicShapeRenderer {
    /**
     * Dokončí vykreslování a vše flushne na obrazovku.
     * */
    public void end();

    /**
     * Nastartuje a vrátí instanci {@link ShapeRenderer}u.
     *
     * @return Instance {@link ShapeRenderer} připravená kreslit na obrazovku
     * */
    public ShapeRenderer getShape();
}
