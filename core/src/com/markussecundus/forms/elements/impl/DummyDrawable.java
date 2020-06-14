package com.markussecundus.forms.elements.impl;

import com.markussecundus.forms.utils.vector.VectUtil;


/**
 * The most basic implementation of {@link com.markussecundus.forms.elements.Drawable} that doesn't render anything to the screen.
 *
 * @author MarkusSecundus
 * */
public class DummyDrawable<Rend, Pos, Scalar extends Comparable<Scalar>> extends BasicAbstractDrawable<Rend, Pos, Scalar> {

    /**
     * Must be provided with the <code>VectUtil</code> explicitly.
     * */
    public DummyDrawable(VectUtil<Pos, Scalar> vectUtil, Pos prefSize){
        this(vectUtil, vectUtil.MAX_VAL(), vectUtil.ZERO(), prefSize);
    }

    /**
     * Must be provided with the <code>VectUtil</code> explicitly.
     * */
    public DummyDrawable(VectUtil<Pos, Scalar> vectUtil, Pos maxSize, Pos minSize, Pos prefSize){
        super(vectUtil, maxSize, minSize, prefSize);
        this.VECT_UTIL = vectUtil;
    }

    /**
     * Does nothing.
     * */
    @Override
    public void draw(Rend renderer, Pos position) {}

    /**
     * {@inheritDoc}.
     * */
    @Override
    public VectUtil<Pos, Scalar> getVectUtil() {
        return VECT_UTIL;
    }

    /**
     * Does nothing.
     * */
    @Override
    public void update(float delta, int frameId) {}

    private final VectUtil<Pos, Scalar> VECT_UTIL;
}
