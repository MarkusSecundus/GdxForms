package com.markussecundus.forms.elements.impl;

import com.markussecundus.forms.elements.DrawableElem;
import com.markussecundus.forms.elements.impl.utils.DefaultSizeBehavior;
import com.markussecundus.forms.extensibility.IExtensible;
import com.markussecundus.forms.utils.vector.VectDecomposer;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;


/**
 * The most basic base class for implementations of {@link DrawableElem}.
 *
 * Provides the default size behavior.
 *
 * @param <Rend> The renderer type that performs the drawing of the element to the screen or anywhere else
 * @param <Pos> Vector type used to define the position and dimensions of the element
 * @param <Scalar> The type that Pos's individual components consist of (- see {@link VectUtil} )
 *
 * @see DrawableElem
 * @see DefaultSizeBehavior
 *
 * @author MarkusSecundus
 * */
public abstract class BasicAbstractDrawableElem<Rend, Pos, Scalar extends Comparable<Scalar>> extends IExtensible implements DrawableElem<Rend, Pos> {

    /**
     * Constructs the Properties associated with the size behavior of this.
     *
     * @param max default value for <code>maxSize</code> Property
     * @param min default value for <code>minSize</code> Property
     * @param pref default value for <code>prefSize</code> Property
     * @param decomp <code>Pos</code> decomposer to initilize the {@link DefaultSizeBehavior} component
     * */
    public BasicAbstractDrawableElem(VectDecomposer<Pos, Scalar> decomp, Pos max, Pos min, Pos pref){
        this.sizeBehavior = DEFAULT_SIZE_BEHAVIOR__FACTORY(decomp, max, min, pref);
    }

    @Override
    public ReadonlyProperty<Pos> size() {
        return sizeBehavior.realSize;
    }

    @Override
    public Property<Pos> maxSize() {
        return sizeBehavior.maxSize;
    }

    @Override
    public Property<Pos> minSize() {
        return sizeBehavior.minSize;
    }

    @Override
    public Property<Pos> prefSize() {
        return sizeBehavior.prefSize;
    }

    @Override
    public Property<Pos> _sizeConstraint() {
        return sizeBehavior.sizeConstraint;
    }

    @Override
    public abstract VectUtil<Pos, Scalar> getVectUtil();


    /**
     * Override this if you want to specify the instance of {@link DefaultSizeBehavior} being used by the {@link DrawableElem} yourself.
     *
     * @param decomp {@link VectDecomposer} to be used by the {@link DefaultSizeBehavior}
     * @param max default value for <code>maxSize</code> property
     * @param min default value for <code>minSize</code> property
     * @param pref default value for <code>prefSize</code> property
     *
     * @return <code>new DefaultSizeBehavior[Pos, Scalar](max, min, pred, decomp)</code>
     * */
    protected DefaultSizeBehavior<Pos, Scalar> DEFAULT_SIZE_BEHAVIOR__FACTORY(VectDecomposer<Pos, Scalar> decomp, Pos max, Pos min, Pos pref){
        return new DefaultSizeBehavior<>(max, min, pref, decomp);
    }

    /**
     * The object implementing the basic behavior pattern for sizes of Drawables.
     * */
    protected final DefaultSizeBehavior<Pos, Scalar> sizeBehavior;
}
