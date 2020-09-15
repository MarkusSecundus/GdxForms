package com.markussecundus.forms.elements;

import com.markussecundus.forms.extensibility.Extensible;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import java.util.List;


/**
 * Basic inteface for any {@link Element} that groups together multiple other Elements.
 *
 * @see com.markussecundus.forms.elements.Element
 * @see UberDrawable
 *
 * @author MarkusSecundus
 * */
public interface UberElement extends Element, Extensible {

    /**
     * Instance of {@link java.util.List} containing only and all the children, that are to be updated in the game loop.
     *
     * Use it to add children to this, to remove them or to change their arrangement in any other way possible.
     * */
    public ConstProperty<List<Element>> children();

    /**
     * Shinier shortcut for calling <code>children().get()</code>.
     * */
    public default List<Element> getChildren(){
        return children().get();
    }


    /**
     * {@inheritDoc}
     *
     * Default implementation distributes the update to all the children.
     */
    @Override default void update(float delta, int frameId){
        for(Element e: children().get())
            e.update(delta, frameId);
    }

}
