package com.markussecundus.forms.elements.impl.layouts;

import com.markussecundus.forms.elements.Drawable;
import com.markussecundus.forms.utils.datastruct.DefaultDict;
import com.markussecundus.forms.utils.vector.VectUtil;

import java.util.Map;


/**
 * The most basic and primitive implementation of {@link com.markussecundus.forms.elements.impl.BasicAbstractDrawable},
 * that has the child positions set directly and doesn't apply any logic
 * for restraining its children to not exceed the borders set to the layout.
 *
 *
 * @param <Rend> The renderer type that performs the drawing of the element to the screen or anywhere else
 * @param <Pos> Vector type used to define the position and dimensions of the element
 * @param <Scalar> The type that Pos's individual components consist of (- see {@link VectUtil} )
 *
 * @see Drawable
 * @see BasicAbstractLayout
 * @see BasicLinearLayout
 *
 * @author MarkusSecundus
 * */
public class PrimitivePositionalLayout<Rend, Pos, Scalar extends Comparable<Scalar>> extends BasicAbstractLayout<Rend, Pos, Scalar> {
//public:

    /**
     * Uses <code>posUtil.ZERO()</code> as value for <code>neutralPos</code>.
     * Initialises the size behavior and children management.
     * Constructs the Properties associated with the size behavior of this.
     *
     * @param maxSize default value for <code>maxSize</code> Property
     * @param minSize default value for <code>minSize</code> Property
     * @param prefSize default value for <code>prefSize</code> Property
     * @param posUtil {@link VectUtil} for the vector type used
     * */
    public PrimitivePositionalLayout(Pos maxSize, Pos minSize, Pos prefSize, VectUtil<Pos, Scalar> posUtil) {
        this(maxSize, minSize, prefSize, posUtil, posUtil.ZERO());
    }

    /**
     * Initialises the size behavior and children management.
     * Constructs the Properties associated with the size behavior of this.
     *
     * @param maxSize default value for <code>maxSize</code> Property
     * @param minSize default value for <code>minSize</code> Property
     * @param prefSize default value for <code>prefSize</code> Property
     * @param posUtil {@link VectUtil} for the vector type used
     * @param neutralPos default position set to every children when it's added if it doesn't have position already set explicitly
     * */
    public PrimitivePositionalLayout(Pos maxSize, Pos minSize, Pos prefSize, VectUtil<Pos, Scalar> posUtil, Pos neutralPos){
        super(maxSize, minSize, prefSize, posUtil);
        this.NEUTRAL_POS = neutralPos;
    }

    /**
     * Sets position for the child relative to the layout's origin.
     * The stored position will be preserved even upon child's removal.
     *
     * @param child element whose position is being set
     * @param newPos position realative to the layout's origin
     * */
    public Pos setChildPosition(Drawable<Rend, Pos> child, Pos newPos){
        return childPositionsContainer.get(child).positionWriter.set(newPos);
    }


//protected:

    /**
     * Default position set to every children when it's added if it doesn't have position already set explicitly
     * */
    protected final Pos NEUTRAL_POS;

    /**
     * Ensures that the child positions are stored in {@link DefaultDict} and initialized with <code>NEUTRAL_POS</code>
     * */
    @Override protected Map<Drawable<Rend, Pos>, ChildPosition> MAKE_CONTAINER_FOR_CHILD_POSITIONS() {
        return new DefaultDict<>(e->new ChildPosition(NEUTRAL_POS));
    }


}
























