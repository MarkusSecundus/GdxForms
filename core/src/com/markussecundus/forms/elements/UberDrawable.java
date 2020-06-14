package com.markussecundus.forms.elements;

import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;

import java.util.List;


/**
 * Basic inteface for any {@link Drawable} that groups together multiple other Drawables.
 *
 * @param <Pos> Vector type used to define the position and dimensions of the element
 * @param <Rend> The renderer type that performs the drawing of the element to the screen or anywhere else
 *
 * @author MarkusSecundus
 * */
public interface UberDrawable<Rend, Pos> extends Drawable<Rend, Pos>, UberElement {


    /**
     * Instance of {@link java.util.List} containing only and all the children, that are to be drawn on the screen.
     *
     * Use it to add children to this, to remove them or to change their arrangement in any other way possible.
     *
     * All the children in here will be automatically present also in <code>children</code>.
     * Try to avoid simultaneous modifications of both the lists.
     * @see com.markussecundus.forms.elements.impl.utils.ElementLists
     * */
    public ConstProperty<List<Drawable<Rend, Pos>>> drawableChildren();

    /**
     * Shinier shortcut for calling <code>drawableChildren().get()</code>.
     * */
    public default List<Drawable<Rend, Pos>> getDrawableChildren(){
        return drawableChildren().get();
    }

    /**
     * Returns the position of <code>child</code> relative to the parent (<code>this</code>).
     *
     * Can return <code>null</code> if <code>child</code> isn't really the child of <code>this</code>.
     *
     * @param child the child of this whose position is being obtained
     * */
    public ReadonlyProperty<Pos> childPosition(Drawable<Rend, Pos> child);


    /**
     * Shinier shortcut for calling <code>getChildPosition().get(child)</code>.
     *
     * @param child the child of this whose position is being obtained
     * */
    public default Pos getChildPosition(Drawable<Rend, Pos> child){
        return childPosition(child).get();
    }
}
