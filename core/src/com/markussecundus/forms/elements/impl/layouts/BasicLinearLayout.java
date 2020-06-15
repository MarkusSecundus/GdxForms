package com.markussecundus.forms.elements.impl.layouts;

import com.markussecundus.forms.elements.Drawable;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;
import com.markussecundus.forms.wrappers.property.impl.general.ArrayProperty;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic layout for arranging Elements in line.
 *
 * @see BasicAbstractLayout
 *
 * @author MarkusSecundus
 * */
public class BasicLinearLayout<Rend, Pos, Scalar extends Comparable<Scalar>> extends BasicAbstractLayout<Rend, Pos, Scalar> {
//public:
    /**
     * Inits the layout with default values.
     *
     * maxSize: vectUtil.MAX_VALUE()
     * minSize: vectUtil.ZERO()
     * outerPadding: vectUtil.ZERO()
     * innerPadding: vectUtil.ZERO_SCALAR()
     * dimension: 0
     * allignments: [0,0,...,0]
     *
     * @param prefSize default value for layout's preffered size
     * @param posUtil {@link VectUtil} for the used vector type
     * */
    public BasicLinearLayout(Pos prefSize, VectUtil<Pos,Scalar> posUtil){
        this(posUtil.MAX_VAL(),posUtil.ZERO(), prefSize, posUtil, posUtil.ZERO(), posUtil.ZERO_SCALAR(), 0, FormsUtil.fillArray(new Double[posUtil.DIMENSION_COUNT()], 0d));
    }
    /**
     * Inits the layout with default values.
     *
     * @param maxSize initial value for layout's max size
     * @param minSize initial value for layout's min size
     * @param prefSize initial value for layout's preffered size
     * @param posUtil {@link VectUtil} for the used vector type
     * @param outerPadding initial value for both of layout's outer paddings
     * @param innerPadding initial value for layout's inner paddings
     * @param dimension initial value for the dimension in which the linearity is applied
     * @param allignments initial values for allignments of children elements; must be values of interval [0d, 1d]
     * */
    public BasicLinearLayout(Pos maxSize, Pos minSize, Pos prefSize, VectUtil<Pos, Scalar> posUtil, Pos outerPadding, Scalar innerPadding, int dimension, Double[] allignments){
        super(maxSize, minSize, prefSize, posUtil);
        this.outerPaddingBegin = new SimpleProperty<>(outerPadding);
        this.outerPaddingEnd = new SimpleProperty<>(outerPadding);
        this.innerPadding = new SimpleProperty<>(innerPadding);
        this.dimension = new SimpleProperty<>(dimension);
        this.allignments = new ArrayProperty<>(allignments);



        EventListener<Object> dirty_marker = e->{
            markChildSizesAsDirty();
            markChildPositionsAsDirty();
            return true;
        };
        EventListener<Object> positions_dirty = o->{
            markChildPositionsAsDirty();
            return true;
        };

        onChildResizedAction.getListeners().add(positions_dirty);
        this.allignments._massSetterUtilDelegate().getListeners().add(positions_dirty);

        size().getSetterListeners()._getUtilListeners().add(dirty_marker);
        childrenContainer.onDrawableAdded.getListeners().add(dirty_marker);

        childrenContainer.onElementRemoved.getListeners().add(e->{
            if(_isDrawableChild(e)) {
                markChildSizesAsDirty();
                markChildPositionsAsDirty();
            }
            return true;
        });
        outerPaddingBegin().getSetterListeners()._getUtilListeners().add(dirty_marker);
        outerPaddingEnd().getSetterListeners()._getUtilListeners().add(dirty_marker);
        innerPadding().getSetterListeners()._getUtilListeners().add(dirty_marker);
        dimension().getSetterListeners()._getUtilListeners().add(dirty_marker);
    }

    /**
     * @return the padding on the left/bottom side of the layout
     * */
    public Property<Pos> outerPaddingBegin(){ return outerPaddingBegin; }
    /**
     * @return the padding on the right/top side of the layout
     * */
    public Property<Pos> outerPaddingEnd(){ return outerPaddingEnd; }
    /**
     * @return the padding between separate elements
     * */
    public Property<Scalar> innerPadding(){return innerPadding;}

    /**
     * @return the dimension in which the linearity of the layout is applied
     * */
    public Property<Integer> dimension(){return dimension;}

    /**
     * @return allignment for each dimension with which the children are arranged
     * */
    public Property<Double> allignment(int dimension){return allignments.at(dimension);}

    /**
     * @return Shinier shortcut for <code>outerPaddingBegin().get()</code>
     * */
    public Pos getOuterPaddingBegin(){return outerPaddingBegin().get();}
    /**
     * @return Shinier shortcut for <code>outerPaddingEnd().get()</code>
     * */
    public Pos getOuterPaddingEnd(){return outerPaddingEnd().get();}
    /**
     * @return Shinier shortcut for <code>innerPadding().get()</code>
     * */
    public Scalar getInnerPadding(){return innerPadding().get();}
    /**
     * @return Shinier shortcut for <code>dimension().get()</code>
     * */
    public int getDimension(){return dimension().get();}
    /**
     * @return Shinier shortcut for <code>allignment(dimension).get()</code>
     * */
    public double getAlignment(int dimension){return allignment(dimension).get();}



    /**
     * @return Shinier shortcut for <code>outerPaddingBegin().set(newPadding)</code>
     * */
    public Pos setOuterPaddingBegin(Pos newPadding){return outerPaddingBegin.set(newPadding);}
    /**
     * @return Shinier shortcut for <code>outerPaddingEnd().set(newPadding)</code>
     * */
    public Pos setOuterPaddingEnd(Pos newPadding){return outerPaddingEnd.set(newPadding);}
    /**
     * Shinier shortcut for setting both <code>setOuterPaddingBegin</code> and <code>setOuterPaddingEnd</code> to <code>newPadding</code> value
     * */
    public void setOuterPadding(Pos newPadding){setOuterPaddingBegin(newPadding);setOuterPaddingEnd(newPadding);}
    /**
     * @return Shinier shortcut for <code>innerPadding().set(newPadding)</code>
     * */
    public Scalar setInnerPadding(Scalar newPadding){return innerPadding.set(newPadding);}
    /**
     * @return Shinier shortcut for <code>dimension().set(newDimensionOfLinearity)</code>
     * */
    public int setDimension(int newDimensionOfLinearity){return dimension().set(newDimensionOfLinearity);}
    /**
     * @return Shinier shortcut for <code>allignment(dimension).set(newAllignment)</code>
     * */
    public double setAlignment(int dimension, double newAlignment){ return allignment(dimension).set(newAlignment); }

    /**
     * {@inheritDoc}
     * */
    @Override
    public ReadonlyProperty<Pos> childPosition(Drawable<Rend, Pos> child) {
        applySizeChanges();
        return super.childPosition(child);
    }

    /**
     * @return the ammount of place that the container elements and padding really fill on the screen in the way they are arranged by the layout
     * */
    public Pos computeRealSize(){
        Pos outer_padding = POS.add(getOuterPaddingBegin(), getOuterPaddingEnd());
        Scalar[] elemsSize = POS.ZERO_DECOMPOSED();
        Scalar inner_padding = getInnerPadding();
        int linearity_dim = getDimension();

        FormsUtil.checkSufficientNumDimensions(linearity_dim, elemsSize);

        elemsSize[linearity_dim] = POS.subScalar(elemsSize[linearity_dim],inner_padding);

        for(Drawable<Rend, Pos> child: getDrawableChildren()){
            Scalar[] childSize = POS.decompose(child.getSize());
            FormsUtil.checkNumDimensions(elemsSize.length, childSize);

            elemsSize[linearity_dim] = POS.addScalar(POS.addScalar(elemsSize[linearity_dim], childSize[linearity_dim]), inner_padding);

            for(int t=0;t<linearity_dim;++t)
                elemsSize[t] = FormsUtil.max(elemsSize[t], childSize[t]);
            for(int t=linearity_dim+1;t<elemsSize.length;++t)
                elemsSize[t] = FormsUtil.max(elemsSize[t], childSize[t]);
        }
        return POS.add(outer_padding, POS.compose(elemsSize));
    }

    /**the leftmost layout allignment that makes sense*/
    public static final double ALIGNMENT_LEFT = 0d;
    /**exactly centered layout allignment*/
    public static final double ALIGNMENT_CENTRE = 0.5d;
    /**the rightmost layout allignment that makes sense*/
    public static final double ALIGNMENT_RIGHT = 1d;


    /**
     * Checks if child sizes or positions are dirty and optionally recomputes them
     * as when rendering.
     * */
    public void applySizeChanges(){
        if(childSizesDirty)
            onResized();
        if(childPositionsDirty)
            recomputeChildrenPositions();
    }

    /**
     * Marks that child positions need to be recomputed.
     * */
    public final void markChildPositionsAsDirty(){
        this.childPositionsDirty = true;
    }
    /**
     * Marks that child sizes need to be recomputed.
     * */
    public final void markChildSizesAsDirty(){
        this.childSizesDirty = true;
    }

    /**
     * Decides whether the {@link LayoutTooShortException} is thrown at the end of child positions recompute
     * if the children don't fit into the layout, or if it is ignored.
     * */
    public final Property<Boolean> ignoreTooShort = new SimpleProperty<>(false);

    //private:
    private final Property<Integer> dimension;
    private final ArrayProperty<Double> allignments;
    private Double[] getAllAllignments(){return allignments.getArray();}

    private boolean childPositionsDirty = true, childSizesDirty = true;

    private final Property<Pos> outerPaddingBegin, outerPaddingEnd;
    private final Property<Scalar> innerPadding;


    private  boolean _onResizedIsBeingCalled = false;
    private void onResized(){
        if(_onResizedIsBeingCalled)
            throw new StackOverflowError(String.format("onResized method of %s is not supposed to be called recursively!", this));
        _onResizedIsBeingCalled = true;
        try {

            int dim_of_linearity = this.getDimension();
            Pos dims = POS.cpy(getSize());
            dims = POS.sub(POS.sub(dims, getOuterPaddingBegin()), getOuterPaddingEnd());

            Scalar[] constrs = POS.decompose(dims);
            constrs[dim_of_linearity] = POS.MAX_VAL_SCALAR();

            for (Drawable<Rend, Pos> child : getDrawableChildren())
                child._sizeConstraint().set(POS.compose(constrs));

            childSizesDirty = false;
        }finally{_onResizedIsBeingCalled = false;}
    }

    private void recomputeChildrenPositions(){
        List<Drawable<Rend,Pos>> drw_children = getDrawableChildren();
        List<Pos> pozice = new ArrayList<>(drw_children.size());
        Pos layout_size = getSize();
        Pos outer_padding_begin = getOuterPaddingBegin();
        Pos outer_padding_end = getOuterPaddingEnd();
        Scalar inner_padding = getInnerPadding();
        int dim_of_linearity = getDimension();
        Double[] allignments = getAllAllignments();
        Pos size_without_outer_padding = POS.sub(POS.sub(POS.cpy(layout_size), outer_padding_begin), outer_padding_end);

        Scalar line = POS.getNth(outer_padding_begin, dim_of_linearity);

        for(Drawable<Rend, Pos> child : drw_children){
            Pos child_size = child.getSize();
            Pos child_pos = POS.sub(POS.cpy(size_without_outer_padding), child_size);

            child_pos = POS.sclComponents(child_pos, allignments);
            child_pos = POS.add(child_pos, outer_padding_begin);
            child_pos = POS.withNth(child_pos, dim_of_linearity, line);

            line = POS.addScalar(line, POS.getNth(child_size, dim_of_linearity));
            line = POS.addScalar(line, inner_padding);

            pozice.add(child_pos);
        }
        line = POS.subScalar(line, inner_padding);

        Scalar layout_length = POS.getNth(layout_size, dim_of_linearity);
        layout_length = POS.subScalar(layout_length, POS.getNth(outer_padding_end, dim_of_linearity));

        Scalar length_to_redistribute = POS.subScalar(layout_length, line);

        if(allignments[dim_of_linearity]>0d && !length_to_redistribute.equals(POS.ZERO_SCALAR())) {
            length_to_redistribute = POS.sclScalar(length_to_redistribute, allignments[dim_of_linearity]);

            for (int t=0;t<pozice.size();++t)
                pozice.set(t, POS.withNth(pozice.get(t),dim_of_linearity, POS.addScalar(POS.getNth(pozice.get(t), dim_of_linearity),length_to_redistribute)));
        }

        for(int t=0;t<pozice.size();++t)
            setChildPosition(drw_children.get(t), pozice.get(t));

        childPositionsDirty = false;
        if(!ignoreTooShort.get() && line.compareTo(layout_length)>0 ) //v případě, že se všechny prvky nevejdou do layoutu, vyhodíme výjímku, ale až úplně na konci, když je všechno hotovo, aby to případně uživatel mohl ignorovat
            throw new LayoutTooShortException();
    }


    /**
     * Thrown by {@link BasicLinearLayout} on children positions recomputation when the children don't fit into the layout
     * */
    public static class LayoutTooShortException extends RuntimeException{}
}
