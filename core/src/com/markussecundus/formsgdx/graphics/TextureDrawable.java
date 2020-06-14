package com.markussecundus.formsgdx.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.markussecundus.forms.gfx.BinaryGraphicalComposite;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;

/**
 * Jednoduchý obdélník s texturou vykreslitelný v enginu LibGDX.
 *
 * @see GraphicalPrimitive
 * @see ShapeRenderer
 * @see com.markussecundus.formsgdx.rendering.BasicRenderer
 * @see RoundedRectangle
 * @see BasicRectangle
 * @see Vect2f
 * @see Color
 *
 * @author MarkusSecundus
 * */
public class TextureDrawable implements GraphicalPrimitive<Batch, Vect2f, Float> {
//public:
    /**
     * Inicializuje útvar danou {@link Texture} a jejími výchozími rozměry.
     * */
    public TextureDrawable(Texture texture){this(texture, Vect2f.make(texture.getWidth(), texture.getHeight()));}

    /**
     * Inicializuje útvar danými hodnotami.
     * */
    public TextureDrawable(Texture texture, Vect2f dims){
        this.dimensions = new SimpleProperty<>(dims);
        this.texture = new SimpleProperty<>(texture);
    }

    /**
     * Vykreslí texturu na danou pozici na obrazovce.
     *
     * {@inheritDoc}
     * */
    @Override public boolean draw(Batch renderer, Vect2f pos) {
        Vect2f dims = getDimensions();
        Texture t = getTexture();

        renderer.draw(t, pos.x, pos.y, dims.x, dims.y);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return rozměry v kterých bude daná textura vykreslována
     * */
    @Override public Property<Vect2f> dimensions() { return dimensions; }

    /**
     * @return textura tohoto útvaru
     * */
    public Property<Texture> texture(){return texture;}

    /**
     * @return Pohodlnější zkratka za <code>texture().get()</code>
     * */
    public Texture getTexture(){return texture().get();}
    /**
     * @return Pohodlnější zkratka za <code>texture().set(newTexture)</code>
     * */
    public Texture setTexture(Texture newTexture){return texture().set(newTexture);}


    /**{@inheritDoc}*/
    @Override public VectUtil<Vect2f, Float> getVectUtil() {return Vect2f.getUtility();}

//private:
    private final Property<Vect2f> dimensions;
    private final Property<Texture> texture;

}
