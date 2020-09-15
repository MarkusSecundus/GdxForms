package com.markussecundus.forms.elements.impl.layouts;

import com.markussecundus.forms.elements.DrawableElem;
import com.markussecundus.forms.elements.Element;
import com.markussecundus.forms.elements.UberDrawable;
import com.markussecundus.forms.elements.impl.BasicAbstractDrawableElem;
import com.markussecundus.forms.elements.impl.utils.DefaultSizeBehavior;
import com.markussecundus.forms.elements.impl.utils.ElementLists;
import com.markussecundus.forms.elements.impl.utils.ElementsTagMap;
import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.events.ListenerPriorities;
import com.markussecundus.forms.gfx.Drawable;
import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;
import com.markussecundus.forms.wrappers.property.impl.readonly.SimpleReadonlyProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Basic base class for all Layouts.
 *
 * Provides the default size behavior (as in {@link BasicAbstractDrawableElem}) and
 * basic logic for containment and management  of children and their relative positions.
 *
 *
 * @see com.markussecundus.forms.elements.UberElement
 * @see com.markussecundus.forms.elements.UberDrawable
 *
 * @see BasicLinearLayout
 * @see PrimitivePositionalLayout
 *
 * @see DefaultSizeBehavior
 * @see ElementLists
 *
 * @author MarkusSecundus
 * */
public abstract class BasicAbstractLayout<Rend, Pos, Scalar extends Comparable<Scalar>> extends BasicAbstractDrawableElem<Rend, Pos, Scalar> implements UberDrawable<Rend, Pos> {

    /**
     * Initialises the size behavior and children management.
     * Constructs the Properties associated with the size behavior of this.
     *
     * @param maxSize default value for <code>maxSize</code> Property
     * @param minSize default value for <code>minSize</code> Property
     * @param prefSize default value for <code>prefSize</code> Property
     * @param posUtil {@link VectUtil} for the vector type used
     * */
    public BasicAbstractLayout(Pos maxSize, Pos minSize, Pos prefSize, VectUtil<Pos, Scalar> posUtil){
        super(posUtil, maxSize, minSize, prefSize);
        this.POS = posUtil;
    }

    @Override public ConstProperty<List<DrawableElem<Rend, Pos>>> drawableChildren() {
        return childrenContainer.drawablesAsProperty;
    }

    @Override public ConstProperty<List<Element>> children() {
        return childrenContainer.elementsAsProperty;
    }

    @Override public ReadonlyProperty<Pos> childPosition(DrawableElem<Rend, Pos> child) {
        ChildPosition pos = childPositionsContainer.get(child);
        return pos.positionProperty;
    }

    @Override
    public final List<DrawableElem<Rend, Pos>> getDrawableChildren() {
        return UberDrawable.super.getDrawableChildren();
    }

    @Override
    public final Pos getChildPosition(DrawableElem<Rend, Pos> child) {
        return UberDrawable.super.getChildPosition(child);
    }

    /**
     * Renders drawable children on their associated positions relative to <code>position</code> parameter.
     *
     * {@inheritDoc}
     * */
    @Override public boolean draw(Rend renderer, Pos position) {
        for(DrawableElem<Rend, Pos> child: drawableChildren().get())
            child.draw(renderer, POS.add(position, childPosition(child).get()  ) );
        return true;
    }

    @Override public VectUtil<Pos, Scalar> getVectUtil() {
        return POS;
    }


//protected:

    /**
     * {@link VectUtil} for the vector type used in the layout.
     * */
    protected final VectUtil<Pos, Scalar> POS;


    /**
     * For associationg children with random things.
     * @see ElementsTagMap
     * */
    protected final ElementsTagMap<Element> childrenByTag = new ElementsTagMap<>();

    /**
     * For associationg drawable children with random things.
     * (eg. Drawable children with their <code>size</code> Properties)
     *
     * @see ElementsTagMap
     * */
    protected final ElementsTagMap<DrawableElem<Rend, Pos>> drawableChildrenByTag = new ElementsTagMap<>();

    /**
     * Add any logic here, that needs to be applied whenever a drawable child changes its size.
     * */
    protected final EventDelegate<ReadonlyProperty.SetterListenerArgs<Pos>> onChildResizedAction = EventDelegate.make();


    /**
     * Container class for all the listeners, that are automatically added
     * to the delegates on <code>childrenContainer</code>, to make them easily refferentiable
     * and possibly able to be deleted from the particular delegate if necessary.
     * */
    protected final class ChildrenListeners {
        /**
         * Skips execution of the listener for all Objects attempted to be removed, that aren't instances of {@link Element}.
         * <p>
         * Gets added to <code>childrenContainer.onElementRemoved</code>
         * */
        public final EventListener<Object> IS_ELEMENT_GUARD = o-> o instanceof Element;

        /**
         * Sets the <code>_sizeConstraint</code> of the drawable child being removed to null.
         *<p>
         *Gets added to <code>childrenContainer.onElementRemoved</code>
         * */
        public final EventListener<Object> SIZE_CONSTRAINT_REMOVER__ON_UNCHILD = o -> {
            if (_isDrawableChild(o)) {
                DrawableElem<?, ?> drw = (DrawableElem<?, ?>) o;
                ((EventDelegate<?>) (drw.size().setterListeners().get()))._getUtilListeners().remove(onChildResizedAction);
                drw._sizeConstraint().set(null);
            }
            return true;
        };

        /**
         * Adds the <code>onChildResizedAction</code> delegate to the setter listener of <code>size</code> property
         * of the drawable child being added.
         * */
        public final EventListener<Drawable<Rend, Pos>> ADDER_OF__ON_RESIZED_LISTENER = drw -> {
            drw.size().setterListeners().get()._getUtilListeners().add(onChildResizedAction);
            return true;
        };

        /**
         * Removes the <code>onChildResizedAction</code> delegate from the setter listener of <code>size</code> property
         * of the drawable child being removed.
         * */
        public final EventListener<Object> REMOVER_OF__ON_RESIZED_LISTENER = drw -> {
            if(_isDrawableChild(drw)) {
                ((DrawableElem<?,?>)drw).size().setterListeners().get()._getUtilListeners().remove(onChildResizedAction);
            }
            return true;
        };
    }
    /**
     * The instance of {@link ChildrenListeners} belonging to the particular instance of <code>BasicAbstractLayout</code>.
     * */
    protected final ChildrenListeners CHILDREN_LISTENERS = new ChildrenListeners();


    /**
     * The component to which the logic for children containment is delegated.
     *
     * On construction already has basic listeners added,
     * that remove any <code>sizeConstraint</code> from a child when it is removed,
     * and bind the drawable children with their <code>size</code> Property as a tag.
     *
     *
     * @see ElementLists
     * */
    protected final ElementLists.Delegated<Rend, Pos> childrenContainer = new ElementLists.Delegated<>();
    {
        childrenContainer.onElementRemoved.getListeners(ListenerPriorities.ARG_GUARD).add(CHILDREN_LISTENERS.IS_ELEMENT_GUARD);
        childrenContainer.onElementRemoved._getUtilListeners().add(CHILDREN_LISTENERS.SIZE_CONSTRAINT_REMOVER__ON_UNCHILD);

        childrenContainer.onDrawableAdded._getUtilListeners().add(CHILDREN_LISTENERS.ADDER_OF__ON_RESIZED_LISTENER);
        childrenContainer.onElementRemoved._getUtilListeners().add(CHILDREN_LISTENERS.REMOVER_OF__ON_RESIZED_LISTENER);

        setDrawableChildToTagBinding(DrawableElem::size);
    }




    /**
     * Creates the <code>childPositionsContainer</code> container used to store positions of drawable children.
     *
     * Override this when needed to use something else than {@link HashMap}.
     * */
    protected Map<DrawableElem<Rend, Pos>, ChildPosition> MAKE_CONTAINER_FOR_CHILD_POSITIONS(){return new HashMap<>(); }

    /**
     * Stores positions of drawable children.
     *
     * Key... the drawable child
     * Value... its position
     * */
    protected final Map<DrawableElem<Rend, Pos>, ChildPosition> childPositionsContainer = MAKE_CONTAINER_FOR_CHILD_POSITIONS();

    /**
     * Associates a {@link DrawableElem} child with a position in this Layout.
     *
     * @param child The child to which the position is being given
     * @param pos The position being given to the <code>child</code>
     * */
    protected Pos setChildPosition(DrawableElem<Rend, Pos> child, Pos pos){
        ChildPosition child_pos = childPositionsContainer.get(child);
        if(child_pos==null) {
            childPositionsContainer.put(child, new ChildPosition(pos));
            return pos;
        }
        else
            return child_pos.positionWriter.set(pos);
    }

    /**
     * Data class for containing both the {@link ReadonlyProperty} of child size
     * and its setter.
     * */
    protected class ChildPosition{
        public ChildPosition(Pos initValue){
            Pair<SimpleReadonlyProperty<Pos>, WriteonlyWrapper<Pos>> pos = SimpleReadonlyProperty.make(initValue);
            positionProperty = pos.first();
            positionWriter = pos.second();
        }

        /**
         * Property encapsulationg the child's position.
         * */
        public final ReadonlyProperty<Pos> positionProperty;

        /**
         * Setter for <code>positionProperty</code>.
         * */
        public final WriteonlyWrapper<Pos> positionWriter;
    }

    /**
     * Determines whether an Object is a child of <code>this</code>, that is set to be drawn on screen.
     *
     * @param o the object to recognize
     * */
    protected final boolean _isDrawableChild(Object o){
        return o instanceof DrawableElem && getDrawableChildren().contains(o);
    }

    /**
     * Determines whether an Object is a child of <code>this</code>.
     *
     * @param o the object to recognize
     * */
    protected final boolean _isChild(Object o){
        return o instanceof Element && getChildren().contains(o);
    }

    /**
     * Ensures that all newly added drawable children will be bound to a tag in <code>drawableChildrenByTag</code>
     * specified by the <code>tagGetter</code> function.
     *
     * @param tagGetter logic for extracting tag object from a <code>DrawableElem</code>.
     * */
    protected final void setDrawableChildToTagBinding(Function<DrawableElem<Rend, Pos>, Object> tagGetter){
        drawableChildrenByTag.bindTag(childrenContainer.onDrawableAdded, childrenContainer.onElementRemoved, tagGetter);
    }









    /**
     * Thrown on recomputation of the children positions when the children do not fit into the layout.
     * */
    public static class LayoutTooShortException extends RuntimeException{}
}
