package com.markussecundus.forms.elements;

import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;


/**
 * Basic interface for any <code>Element</code>, that can be drawn on the screen.
 *
 * @param <Rend> The renderer type that performs the drawing of the element to the screen or anywhere else
 * @param <Pos> Vector type used to define the position and dimensions of the element
 *
 * @see  com.markussecundus.forms.elements.impl.BasicAbstractDrawable
 *
 * @author MarkusSecundus
 * */
public interface Drawable<Rend, Pos> extends Element{

    /**
     * Called each frame, renders the <code>Drawable</code> on the screen.
     *
     * @param renderer The instance of <code>Rend</code> using which the <code>Drawable</code> is to be rendered
     * @param position The position on which the <code>Drawable</code> is supposed to be drawn.
     *                 By convention should define the leftmost-downmost edge of
     *                 the rectangle circumscribed to the this if in 2D space
     * */
    public void draw(Rend renderer, Pos position);

    /**
     * The real size of the <code>Drawable</code>, which it takes when rendered on the screen.
     * Supposedly should be derived from the values of other present size parameters,
     * therefore it cannot be modified directly.
     * @see com.markussecundus.forms.elements.impl.utils.DefaultSizeBehavior
     * */
    public ReadonlyProperty<Pos> size();

    /**
     * The maximum size that the <code>Drawable</code> can attain.
     * Should be strongly greater than or equal to <code>minSize</code>
     * */
    public Property<Pos> maxSize();

    /**
     * The minimum size that the <code>Drawable</code> can attain.
     * Should be strongly lower than or equal to <code>maxSize</code>
     * */
    public Property<Pos> minSize();

    /**
     * The preffered size that the <code>Drawable</code> will have
     * if it is within the bounds of <code>minSize</code> and (<code>maxSize</code>,<code>_sizeConstraint</code>).
     * */
    public Property<Pos> prefSize();

    /**
     * The size constraint that can be applied by the owner of the <code>Drawable</code>.
     *
     * Used eg. if the element is in a layout to regulate its size
     * without affecting the <code>maxSize</code> defined by the user.
     *
     * If null, then no constraint is to be applied.
     *
     * Doesen't have shortcuts implemented for <code>.get</code> and <code>.set</code>
     * to descourage BFU from using it.
     *
     * @see UberElement
     * @see UberDrawable
     * */
    public Property<Pos> _sizeConstraint();


    /**
     * Gets the {@link VectUtil} for the vector type used by this.
     * */
    public VectUtil<Pos, ?> getVectUtil();

    /**
     * Shinier shortcut for calling <code>size().get()</code>.
     * */
    public default Pos getSize(){
        return size().get();
    }

    /**
     * Shinier shortcut for calling <code>maxSize().get()</code>.
     * */
    public default Pos getMaxSize(){
        return maxSize().get();
    }

    /**
     * Shinier shortcut for calling <code>minSize().get()</code>.
     * */
    public default Pos getMinSize(){
        return minSize().get();
    }

    /**
     * Shinier shortcut for calling <code>prefSize().get()</code>.
     * */
    public default Pos getPrefSize(){
        return prefSize().get();
    }

    /**
     * Shinier shortcut for calling <code>maxSize().set(newSize)</code>.
     *
     * @param newSize new value for the Property
     * */
    public default Pos setMaxSize(Pos newSize){return maxSize().set(newSize);}

    /**
     * Shinier shortcut for calling <code>minSize().set(newSize)</code>.
     *
     * @param newSize new value for the Property
     * */
    public default Pos setMinSize(Pos newSize){return minSize().set(newSize);}

    /**
     * Shinier shortcut for calling <code>prefSize().set(newSize)</code>.
     *
     * @param newSize new value for the Property
     * */
    public default Pos setPrefSize(Pos newSize){return prefSize().set(newSize);}


    /**
     * Destroys the resources owned by the <code>Drawable</code>, that need to be destroyed manually.
     *
     * For the sake of convenience provided with blank default implementation,
     * */
    public default void dispose(){}
}
