package com.markussecundus.forms.elements.impl.layouts;

import com.markussecundus.forms.elements.DrawableElem;
import com.markussecundus.forms.events.EventListenerX;
import com.markussecundus.forms.events.ListenerPriorities;
import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.utils.datastruct.ReadonlyList;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;
import com.markussecundus.forms.wrappers.property.impl.general.PropertyMap;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;

/**
 * Basic layout for arranging Elements in a grid.
 *
 * @param <Rend> The renderer type that performs the drawing of the element to the screen or anywhere else
 * @param <Pos> Vector type used to define the position and dimensions of the element
 * @param <Scalar> The type that Pos's individual components consist of (- see {@link VectUtil} )
 *
 *
 * @see BasicAbstractLayout
 * @see BasicLinearLayout
 *
 * @author MarkusSecundus
 * */
public class BasicGridLayout<Rend, Pos, Scalar extends Comparable<Scalar>> extends BasicAbstractLayout<Rend, Pos, Scalar>{
//public:

    /**
     * Inits the layout with default values.
     *<p></p>
     * defaultCellAllignment: [0,... ,0] <p>
     * defaultElementCoords: [0,... ,0]
     *<p></p>
     * defaultCellSize: posUtil.ZERO()
     *<p></p>
     * maxSize: posUtil.MAX_VAL()<p>
     * minSize: posUtil.ZERO()<p>
     * prefSize: posUtil.MAX_VAL()
     *<p></p>
     * @param posUtil {@link VectUtil} for the used vector type
     * */
    public BasicGridLayout(VectUtil<Pos, Scalar> posUtil){
        this(posUtil.MAX_VAL(), posUtil.ZERO(), posUtil.MAX_VAL(), posUtil);
    }

    /**
     * Inits the layout with default values.
     *<p></p>
     * defaultCellAllignment: [0,... ,0]<p>
     * defaultElementCoords: [0,... ,0]<p>
     * defaultCellSize: posUtil.ZERO()
     *<p></p>
     * @param maxSize initial value for layout's max size
     * @param minSize initial value for layout's min size
     * @param prefSize initial value for layout's preffered size
     * @param posUtil {@link VectUtil} for the used vector type
     * */
    public BasicGridLayout(Pos maxSize, Pos minSize, Pos prefSize, VectUtil<Pos, Scalar> posUtil){
        this(maxSize, minSize, prefSize, posUtil, posUtil.ZERO());
    }
    /**
     * Inits the layout with default values.
     *<p></p>
     * defaultCellAllignment: [0,..,,0]<p>
     * defaultElementCoords: [0,..,,0]
     *<p></p>
     * @param maxSize initial value for layout's max size
     * @param minSize initial value for layout's min size
     * @param prefSize initial value for layout's preffered size
     * @param posUtil {@link VectUtil} for the used vector type
     * @param defaultCellSize initial value for <code>defaultCellSize</code> property
     * */
    public BasicGridLayout(Pos maxSize, Pos minSize, Pos prefSize, VectUtil<Pos, Scalar> posUtil, Pos defaultCellSize){
        this(maxSize, minSize, prefSize, posUtil, defaultCellSize,ReadonlyList.ofItem(0d, posUtil.DIMENSION_COUNT()), ReadonlyList.ofItem(0, posUtil.DIMENSION_COUNT()));
    }

    /**
     * Inits the layout.
     *
     * @param maxSize initial value for layout's max size
     * @param minSize initial value for layout's min size
     * @param prefSize initial value for layout's preffered size
     * @param posUtil {@link VectUtil} for the used vector type
     * @param defaultCellSize initial value for <code>defaultCellSize</code> property
     * @param defaultCellAllignment initial value for <code>defaultCellAllignment</code> property
     * @param defaultElementCoords coordinates where every drawable child with no position specified will be drawn
     * */
    public BasicGridLayout(Pos maxSize, Pos minSize, Pos prefSize, VectUtil<Pos, Scalar> posUtil, Pos defaultCellSize, ReadonlyList.Double defaultCellAllignment, ReadonlyList.Int defaultElementCoords) {
        super(maxSize, minSize, prefSize, posUtil);

        this.defaultCellSize = new SimpleProperty<>(defaultCellSize);

        this.defaultCellAllignment = new SimpleProperty<>(defaultCellAllignment);

        this.collumnSizes = new PropertyMap<>(dimAndCollumn ->new SimpleProperty<>(posUtil.getNth(getDefaultCellSize(), dimAndCollumn.first())));

        this.collumnAllignments = new PropertyMap<>(dimAndCollumn ->new SimpleProperty<>(getDefaultCellAllignment().getNth(dimAndCollumn.first())));

        this.childCells = new PropertyMap<>(elem -> new SimpleProperty<>(defaultElementCoords));

        EventListenerX<Object> dirtyMarker = o->markChildPositionsAndSizesAsDirty();

        this.childrenContainer.onDrawableAdded._getUtilListeners().add(dirtyMarker);
        this.collumnSizes.massSetterDelegate(ListenerPriorities.PRE_UTIL).add(dirtyMarker);
        this.collumnAllignments.massSetterDelegate(ListenerPriorities.PRE_UTIL).add(dirtyMarker);
        this.childCells.massSetterDelegate(ListenerPriorities.PRE_UTIL).add(dirtyMarker);
        this.size().getSetterListeners()._getUtilListeners().add(dirtyMarker);
    }




    /**
     * @return default values for <code>collumnSize</code> by the dimension
     * */
    public Property<Pos> defaultCellSize(){return defaultCellSize;}

    /**
     * @return default values for <code>collumnAllignment</code> by the dimension
     * */
    public Property<ReadonlyList.Double> defaultCellAllignment(){return defaultCellAllignment;}

    /**
     * @return length of the specified collumn of the grid in the specified dimension
     * */
    public Property<Scalar> collumnSize(int dim, int collumn){return collumnSizes.get(Pair.make(dim, collumn));}

    /**
     * @return allignment (value of interval [0;1]) of the specified collumn of the grid in the specified dimension
     * */
    public Property<Double> collumnAllignment(int dim, int collumn){return collumnAllignments.get(Pair.make(dim, collumn));}

    /**
     * @return the cell where the specified drawable child is contained
     * */
    public Property<ReadonlyList.Int> childCell(DrawableElem<Rend, Pos> drw){return childCells.get(drw);}


    /**
     * @return Shinier shortcut for <code>defaultCellSize().get()</code>
     * */
    public final  Pos getDefaultCellSize(){return defaultCellSize().get();}
    /**
     * @return Shinier shortcut for <code>defaultCellAllignment().get()</code>
     * */
    public final  ReadonlyList.Double getDefaultCellAllignment(){return defaultCellAllignment().get();}
    /**
     * @return Shinier shortcut for <code>collumnSize(dim, collumn).get()</code>
     * */
    public final  Scalar getCollumnSize(int dim, int collumn){return collumnSize(dim, collumn).get();}
    /**
     * @return Shinier shortcut for <code>collumnAllignment(dim, collumn).get()</code>
     * */
    public final  Double getCollumnAllignment(int dim, int collumn){return collumnAllignment(dim, collumn).get();}
    /**
     * @return Shinier shortcut for <code>childCell(drw).get()</code>
     * */
    public final  ReadonlyList.Int getChildCell(DrawableElem<Rend, Pos> drw){return childCell(drw).get();}



    /**
     * @return Shinier shortcut for <code>defaultCellSize().set(newVal)</code>
     * */
    public final Pos setDefaultCellSize(Pos newVal){return defaultCellSize().set(newVal);}
    /**
     * @return Shinier shortcut for <code>defaultCellAllignment().set(newVal)</code>
     * */
    public final  ReadonlyList.Double setDefaultCellAllignment(ReadonlyList.Double newVal){return defaultCellAllignment().set(newVal);}
    /**
     * @return Shinier shortcut for <code>defaultCellAllignment().set(newVal)</code> converting varargs array to ReadonlyList.Int
     * */
    public final  ReadonlyList.Double setDefaultCellAllignment(double... newVal){return setDefaultCellAllignment(ReadonlyList.Double.make(newVal));}
    /**
     * @return Shinier shortcut for <code>collumnSize(dim, collumn).set(newVal)</code>
     * */
    public final  Scalar setCollumnSize(int dim, int collumn, Scalar newVal){return collumnSize(dim, collumn).set(newVal);}
    /**
     * @return Shinier shortcut for <code>collumnAllignment(dim, collumn).set(newVal)</code>
     * */
    public final  Double setCollumnAllignment(int dim, int collumn, Double newVal){return collumnAllignment(dim, collumn).set(newVal);}
    /**
     * @return Shinier shortcut for <code>childCell(drw).set(newVal)</code>
     * */
    public final  ReadonlyList.Int setChildCell(DrawableElem<Rend, Pos> drw, ReadonlyList.Int newVal){return childCell(drw).set(newVal);}
    /**
     * @return Shinier shortcut for <code>childCell(drw).set(newVal)</code> converting varargs array to ReadonlyList.Int
     * */
    public final  ReadonlyList.Int setChildCell(DrawableElem<Rend, Pos> drw, int... newVal){return setChildCell(drw, ReadonlyList.Int.make(newVal));}


    /**
     * Adds a drawable child and sets its position on the grid to the specified value
     * in one convenient and easy to read function call.
     *
     * @param drw <code>DrawableElem</code> to be added as child
     * @param positionOnGrid coordinates of the cell where the child is to be added
     * */
    public void addDrawableChild(DrawableElem<Rend, Pos> drw, int... positionOnGrid){
        setChildCell(drw, positionOnGrid);
        getDrawableChildren().add(drw);
    }




    @Override
    public ReadonlyProperty<Pos> childPosition(DrawableElem<Rend, Pos> child) throws LayoutTooShortException{
        if(childPositionsDirty)
            recomputeChildrenConstraintsAndPositions();
        return super.childPosition(child);
    }

    /**
     * Marks that child sizes or positions need to be recomputed.
     * */
    public void markChildPositionsAndSizesAsDirty(){
        childPositionsDirty = true;
    }


    /**
     * Decides whether the {@link BasicAbstractLayout.LayoutTooShortException} is thrown at the end of child positions recompute
     * if the children don't fit into the layout, or if it is ignored.
     * */
    public Property<Boolean> ignoreTooShort(){return ignoreTooShort;}

    /**
     * @return Shinier shortcut for <code>ignoreTooShort().get()</code>
     * */
    public final Boolean getIgnoreTooShort(){return ignoreTooShort().get();}

    /**
     * @return Shinier shortcut for <code>ignoreTooShort().set(newValue)</code>
     * */
    public final Boolean setIgnoreTooShort(Boolean newValue){return ignoreTooShort().set(newValue);}




//private:
    private final Property<Pos> defaultCellSize;

    private final Property<ReadonlyList.Double> defaultCellAllignment;

    private final PropertyMap<Pair<Integer, Integer>, Scalar> collumnSizes;

    private final PropertyMap<DrawableElem<Rend, Pos>, ReadonlyList.Int> childCells;

    private final PropertyMap<Pair<Integer, Integer>, Double> collumnAllignments;

    private boolean childPositionsDirty = false;


    private final Property<Boolean> ignoreTooShort = new SimpleProperty<>(false);





    private void recomputeChildrenConstraintsAndPositions(){
        Pos layoutSize = getIgnoreTooShort() ? null : this.getSize();

        for(DrawableElem<Rend, Pos> child: getDrawableChildren()){
            ReadonlyList.Int childCell = getChildCell(child);
            Pos cellSize = getCellSize(childCell);
            ReadonlyList.Double cellAllignment = getCellAllignment(childCell);

            child._sizeConstraint().set(cellSize);

            Pos cellPos = getCellPosition(childCell);

            Pos childSize = child.getSize();

            Pos spaceForAllignment = POS.sub(POS.cpy(cellSize), childSize);
            spaceForAllignment = POS.sclComponents(spaceForAllignment, cellAllignment);

            Pos childPos = POS.add(cellPos, spaceForAllignment);
            setChildPosition(child, childPos);

            if(layoutSize != null && !POS.le(layoutSize, POS.add(POS.cpy(childPos), childSize)))
                throw new LayoutTooShortException();
        }

        childPositionsDirty = false;
    }



    private Pos getCellSize(ReadonlyList.Int cell){
        POS.checkNumDimensions(cell.size());
        Scalar[] ret = getVectUtil().MAX_VAL_DECOMPOSED();
        for(int dim = 0; dim <cell.size(); ++dim)
            ret[dim] = getCollumnSize(dim, cell.getNth(dim));
        return getVectUtil().compose(ret);
    }

    private ReadonlyList.Double getCellAllignment(ReadonlyList.Int cell){
        POS.checkNumDimensions(cell.size());
        double[] ret = new double[cell.size()];
        for(int dim = 0; dim <cell.size(); ++dim)
            ret[dim] = getCollumnAllignment(dim, cell.getNth(dim));
        return ReadonlyList.Double.make(ret);
    }

    private Pos getCellPosition(ReadonlyList.Int cell){
        POS.checkNumDimensions(cell.size());
        Scalar[] ret = POS.ZERO_DECOMPOSED();

        for(int dim = 0; dim < cell.size(); ++dim){
            Scalar len = ret[dim];
            for(int cellNum = cell.getNth(dim);--cellNum>=0;)
                len = POS.addScalar(len, getCollumnSize(dim, cellNum));

            ret[dim] = len;
        }

        return POS.compose(ret);
    }
}
