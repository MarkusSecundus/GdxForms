package com.markussecundus.forms.elements.impl;

import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.Wrapper;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;


/**
 * A basic {@link com.markussecundus.forms.elements.Drawable} implementation
 * that holds a {@link GraphicalPrimitive} and renders it on the screen as the representation of itself.
 *
 *
 * @param <Pos> Vector type used to define the position and dimensions of the element
 * @param <Rend> The renderer type that performs the drawing of the element to the screen or anywhere else
 * @param <Scalar> The scalar type that the <code>Pos</code> vector consists of
 * @param <Img> The exact type of the {@link GraphicalPrimitive} used as icon
 *
 * @author MarkusSecundus
 * */
public class ImageIcon<Rend, Pos, Scalar extends Comparable<Scalar>, Img extends GraphicalPrimitive<Rend, Pos, Scalar>> extends BasicAbstractDrawable<Rend, Pos, Scalar> {
//public:

    /**
     * inits <code>maxSize</code> and <code>minSize</code> with appropriate extreme values got from the img's {@link VectUtil}
     * @param img the icon that will serve as the graphical representation of this,
     *      *     its dimensions will be only settable through this, throwing {@link RuntimeException} otherwise
     * */
    public ImageIcon(Img img){this(img, img.getVectUtil().MAX_VAL(), img.getVectUtil().ZERO());}

    /**
     * @param img the icon that will serve as the graphical representation of this,
     *            its dimensions will be only settable through this, throwing {@link RuntimeException} otherwise
     * @param maxSize initial value for the <code>maxSize</code> property of this
     * @param minSize initial value for the <code>minSize</code> property of this
     * */
    public ImageIcon(Img img, Pos maxSize, Pos minSize){
        super(img.getVectUtil(), maxSize, minSize, img.getDimensions());
        this.img = img;

        Wrapper<Boolean> editFlag = Wrapper.make(false);
        dimsGuardElem = e->{
            editFlag.set(true);
            try {
                this.img.setDimensions(e.newVal().get());
            }finally{editFlag.set(false);}
            return true;
        };
        size().getSetterListeners()._getUtilListeners().add(dimsGuardElem);

        dimsGuardImg = e->{
            if(!editFlag.get())
                throw new RuntimeException("The image is supposed to be resized only through it's ImageIcon owner");
            return true;
        };
        img.dimensions().getSetterListeners()._getUtilListeners().add(dimsGuardImg);
    }

    /**
     * Draws the icon on the screen.
     * {@inheritDoc}
     * */
    @Override public void draw(Rend renderer, Pos position) {
        img.draw(renderer, position);
    }

    /**
     * Does nothing.
     * {@inheritDoc}
     * */
    @Override public void update(float delta, int frameId) {}


    /**
     * {@inheritDoc}
     * */
    @Override public VectUtil<Pos, Scalar> getVectUtil() {
        return img.getVectUtil();
    }


    /**
     * Disassociates the <code>img</code> object from <code>this</code>.
     * Makes the dimensions of <code>img</code> again settable by anyone.
     * */
    public void disownImg(){
        size().getSetterListeners()._getUtilListeners().remove(dimsGuardElem);
        img.dimensions().getSetterListeners()._getUtilListeners().remove(dimsGuardImg);
    }

    /**
     * The {@link GraphicalPrimitive} instance drawn by this.
     * */
    public final Img img;

//private:
    /**
     * Listener for insuring that noone else than this modifies dimensions if <code>img</code>
     * */
    private final EventListener<Object> dimsGuardImg;
    /**
     * Listener for insuring that noone else than this modifies dimensions if <code>img</code>
     * */
    private final EventListener<ReadonlyProperty.SetterListenerArgs<Pos>> dimsGuardElem;

//public:

    /**
     * Variant parametrized directly with {@link GraphicalPrimitive}
     * instead of any its subtype.
     *
     * {@inheritDoc}
     *
     * @param <Pos> Vector type used to define the position and dimensions of the element
     * @param <Rend> The renderer type that performs the drawing of the element to the screen or anywhere else
     * @param <Scalar> The scalar type that the <code>Pos</code> vector consists of
     *
     * @author MarkusSecundus
     * */
    public static class Plain<Rend, Pos, Scalar extends Comparable<Scalar>> extends ImageIcon<Rend, Pos, Scalar, GraphicalPrimitive<Rend, Pos, Scalar>>{
        public Plain(GraphicalPrimitive<Rend, Pos, Scalar> img){super(img);}
        public Plain(GraphicalPrimitive<Rend, Pos, Scalar> img, Pos maxSize, Pos minSize){super(img, maxSize, minSize);}
    }
}
