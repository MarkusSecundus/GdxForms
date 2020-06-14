package com.markussecundus.forms.elements.impl;

import com.markussecundus.forms.elements.Drawable;
import com.markussecundus.forms.elements.impl.utils.DefaultSizeBehavior;
import com.markussecundus.forms.utils.vector.VectDecomposer;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;


/**
 * The most basic base class for implementations of {@link Drawable}.
 *
 * Provides the default size behavior.
 *
 * @param <Rend> The renderer type that performs the drawing of the element to the screen or anywhere else
 * @param <Pos> Vector type used to define the position and dimensions of the element
 * @param <Scalar> The type that Pos's individual components consist of (- see {@link VectUtil} )
 *
 * @see Drawable
 * @see DefaultSizeBehavior
 *
 * @author MarkusSecundus
 * */
public abstract class BasicAbstractDrawable<Rend, Pos, Scalar extends Comparable<Scalar>> implements Drawable<Rend, Pos> {

    /**
     * Constructs the Properties associated with the size behavior of this.
     *
     * @param max default value for <code>maxSize</code> Property
     * @param min default value for <code>minSize</code> Property
     * @param pref default value for <code>prefSize</code> Property
     * @param decomp <code>Pos</code> decomposer to initilize the {@link DefaultSizeBehavior} component
     * */
    public BasicAbstractDrawable(VectDecomposer<Pos, Scalar> decomp, Pos max, Pos min, Pos pref){
        this.sizeBehavior = new DefaultSizeBehavior<>(max, min, pref, decomp);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public ReadonlyProperty<Pos> size() {
        return sizeBehavior.realSize;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Property<Pos> maxSize() {
        return sizeBehavior.maxSize;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Property<Pos> minSize() {
        return sizeBehavior.minSize;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Property<Pos> prefSize() {
        return sizeBehavior.prefSize;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Property<Pos> _sizeConstraint() {
        return sizeBehavior.sizeConstraint;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public abstract VectUtil<Pos, Scalar> getVectUtil();

    /**
     * The object implementing the basic behavior pattern for sizes of Drawables.
     * */
    protected final DefaultSizeBehavior<Pos, Scalar> sizeBehavior;
}
