package com.markussecundus.forms.elements.impl.layouts;

import com.markussecundus.forms.elements.Drawable;
import com.markussecundus.forms.elements.Element;
import com.markussecundus.forms.elements.UberDrawable;
import com.markussecundus.forms.elements.impl.BasicAbstractDrawable;
import com.markussecundus.forms.elements.impl.utils.DefaultSizeBehavior;
import com.markussecundus.forms.elements.impl.utils.ElementLists;
import com.markussecundus.forms.elements.impl.utils.ElementsTagMap;
import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.utils.Pair;
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
 * Provides the default size behavior (as in {@link BasicAbstractDrawable}) and
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
public abstract class BasicAbstractLayout<Rend, Pos, Scalar extends Comparable<Scalar>> extends BasicAbstractDrawable<Rend, Pos, Scalar> implements UberDrawable<Rend, Pos> {

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

    /**
     * {@inheritDoc}
     * */
    @Override public ConstProperty<List<Drawable<Rend, Pos>>> drawableChildren() {
        return childrenContainer.drawablesAsProperty;
    }

    /**
     * {@inheritDoc}
     * */
    @Override public ConstProperty<List<Element>> children() {
        return childrenContainer.elementsAsProperty;
    }

    /**
     * {@inheritDoc}
     * */
    @Override public ReadonlyProperty<Pos> childPosition(Drawable<Rend, Pos> child) {
        return childPositionsContainer.get(child).positionProperty;
    }

    /**
     * Renders drawable children on their associated positions relative to <code>position</code> parameter.
     *
     * {@inheritDoc}
     * */
    @Override public void draw(Rend renderer, Pos position) {
        for(Drawable<Rend, Pos> child: drawableChildren().get())
            child.draw(renderer, POS.add(position, childPosition(child).get()  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override public VectUtil<Pos, Scalar> getVectUtil() {
        return POS;
    }

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
    protected final ElementsTagMap<Drawable<Rend, Pos>> drawableChildrenByTag = new ElementsTagMap<>();

    /**
     * Add any logic here, that needs to be applied whenever a drawable child changes its size.
     * */
    protected final EventDelegate<ReadonlyProperty.SetterListenerArgs<Pos>> onChildResizedAction = EventDelegate.make();


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
        childrenContainer.onElementRemoved._getUtilListeners().add(
                o -> (o instanceof Element));
        childrenContainer.onElementRemoved._getUtilListeners().add(
                o -> {
                    if (_isDrawableChild(o)) {
                        Drawable<?, ?> drw = (Drawable<?, ?>) o;
                        ((EventDelegate<?>) (drw.size().setterListeners().get()))._getUtilListeners().remove(onChildResizedAction);
                        drw._sizeConstraint().set(null);
                    }
                    return true;
                });

        childrenContainer.onDrawableAdded._getUtilListeners().add(
                drw -> {
                    drw.size().setterListeners().get()._getUtilListeners().add(onChildResizedAction);
                    return true;
                });

        drawableChildrenByTag.bindTag(childrenContainer.onDrawableAdded, childrenContainer.onElementRemoved, Drawable::size);
    }

    /**
     * Creates the <code>childPositionsContainer</code> container used to store positions of drawable children.
     *
     * Override this when needed to use something else than {@link HashMap}.
     * */
    protected Map<Drawable<Rend, Pos>, ChildPosition> MAKE_CONTAINER_FOR_CHILD_POSITIONS(){return new HashMap<>(); }

    /**
     * Stores positions of drawable children.
     *
     * Key... the drawable child
     * Value... its position
     * */
    protected final Map<Drawable<Rend, Pos>, ChildPosition> childPositionsContainer = MAKE_CONTAINER_FOR_CHILD_POSITIONS();

    /**
     * Associates a {@link Drawable} child with a position in this Layout.
     *
     * @param child The child to which the position is being given
     * @param pos The position being given to the <code>child</code>
     * */
    protected Pos setChildPosition(Drawable<Rend, Pos> child, Pos pos){
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
        return o instanceof Drawable && getDrawableChildren().contains(o);
    }

    /**
     * Determines whether an Object is a child of <code>this</code>.
     *
     * @param o the object to recognize
     * */
    protected final boolean _isChild(Object o){
        return o instanceof Element && getChildren().contains(o);
    }
}
