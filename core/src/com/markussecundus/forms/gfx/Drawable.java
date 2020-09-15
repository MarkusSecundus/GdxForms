package com.markussecundus.forms.gfx;

import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;

/**
 * Základní rozhraní pro cokoliv, co má nějaké rozměry a lze to vykreslit.
 *
 * @see com.markussecundus.forms.elements.DrawableElem
 * @see GraphicalPrimitive
 *
 * @author MarkusSecundus
 * */
public interface Drawable<Rend, Pos> {

    /**
     * Called each frame, renders the <code>Drawable</code> on the screen.
     *
     * @param renderer The instance of <code>Rend</code> using which the <code>Drawable</code> is to be rendered
     * @param position The position on which the <code>Drawable</code> is supposed to be drawn.
     *                 By convention should define the leftmost-downmost edge of
     *                 the rectangle circumscribed to the this if in 2D space
     * */
    public boolean draw(Rend renderer, Pos position);


    /**
     * Gets the {@link VectUtil} for the vector type used by this.
     * */
    public VectUtil<Pos, ?> getVectUtil();


    /**
     * The ammount of place the drawable object takes when rendered.
     * */
    public ReadonlyProperty<Pos> size();


    /**
     * Shinier shortcut for calling <code>size().get()</code>.
     * */
    public default Pos getSize(){
        return size().get();
    }
}
