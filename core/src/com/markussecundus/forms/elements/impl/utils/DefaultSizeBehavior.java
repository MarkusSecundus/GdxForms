package com.markussecundus.forms.elements.impl.utils;

import com.markussecundus.forms.elements.DrawableElem;
import com.markussecundus.forms.elements.impl.BasicAbstractDrawableElem;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.utils.vector.VectDecomposer;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;
import com.markussecundus.forms.wrappers.property.impl.general.AbstractProperty;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;
import com.markussecundus.forms.wrappers.property.impl.readonly.SimpleReadonlyProperty;


/**
 * Utility class that implemets the canonical way of how the dimension
 * properties of {@link DrawableElem} should behave.
 *
 * Supposed use is to make an instance of this owned by a {@link DrawableElem}
 * and let the {@link DrawableElem} redirect the calls for its
 * size Properties to this class.
 *
 * The behavior is achieved through {@link EventListener}s added to
 * Properties's <code>.setterListener()._getUtilListeners()</code> (- see: {@link Property}, {@link com.markussecundus.forms.events.EventDelegate})
 *
 * Default behavior:
 *      the <code>realSize</code> attains the value of <code>prefSize</code> in all the dimensions,
 *      where it is within the boundary given by the same dimensions of <code>minSize</code>
 *      and <code>maxSize</code>. Where it is not within the bounds, the crossed bound will
 *      be used instead. If <code>sizeConstraint</code> is set, the lower
 *      of the components of either it or <code>maxSize</code> is used as the higher boundary.
 *
 * @param <Pos> Vector type used to define the position and dimensions of the {@link DrawableElem}
 *               Must by decomposable into separate scalars corresponding to
 *               its individual dimensions by the provided {@link VectDecomposer}.
 * @param <Scalar> The type that Pos's individual components consist of (- see {@link VectUtil} )
 *                must be {@link Comparable} with itself so that it can be determined whether
 *                it is inside the set bounds.
 *
 * @see DrawableElem
 * @see BasicAbstractDrawableElem
 *
 * @author MarkusSecundus
 * */
public class DefaultSizeBehavior<Pos, Scalar extends Comparable<Scalar>> {
 //public:

    /**
     * Creates and sets all the size properties along with the listeners needed for their right functionality.
     *
     * Sets the <code>sizeConstraint</code> to <code>null</code>.
     *
     * @param MAX default value for the upper boundary for the positions
     * @param MIN default value for the lower boundary for the positions
     * @param DEF_PREF default value for preffered size value
     * @param comp the utility used to decompose the currently used type of Pos into its individual components
     * */
    public DefaultSizeBehavior(Pos MAX, Pos MIN, Pos DEF_PREF, VectDecomposer<Pos, Scalar> comp){
        maxPos = MAX;
        minPos = MIN;
        prefPos = DEF_PREF;

        maxSize = new AbstractProperty<Pos>() {
            protected Pos obtain() { return maxPos; }
            protected Pos change(Pos val) { return maxPos=(val==null)?MAX:val; }
        };
        minSize = new AbstractProperty<Pos>() {
            protected Pos obtain() { return minPos; }
            protected Pos change(Pos val) { return minPos=(val==null)?MIN:val; }
        };
        prefSize = new AbstractProperty<Pos>() {
            protected Pos obtain() { return prefPos; }
            protected Pos change(Pos val) { return prefPos=(val==null)?DEF_PREF:val; }
        };
        sizeConstraint = new SimpleProperty<>(null);

        Pair<SimpleReadonlyProperty<Pos>, WriteonlyWrapper<Pos>> realSize_with_setter = SimpleReadonlyProperty.make(DEF_PREF);
        realSize = realSize_with_setter.first();
        realSizeWriter = realSize_with_setter.second();


        EventListener<Property.SetterListenerArgs<?>> onAnySizeChange = e->{
            Scalar[] max = comp.decompose(maxSize.get()), min = comp.decompose(minSize.get()), pref = comp.decompose(prefSize.get());

            {Pos constrPos = sizeConstraint.get();
            if(constrPos!=null){
                Scalar[] constr = comp.decompose(constrPos);

                FormsUtil.checkNumDimensions(max.length, constr.length);

                for(int t=0;t<max.length;++t)
                    max[t] = FormsUtil.min(max[t], constr[t]);
            }}

            FormsUtil.checkNumDimensions(max.length, min.length, pref.length);

            for(int t=0;t<max.length;++t){
                if(max[t].compareTo(min[t]) < 0) {
                    if(e.caller()==maxSize || e.caller()==sizeConstraint) min[t] = max[t];
                    else if(e.caller()==minSize) max[t] = min[t];
                    else throw new IllegalArgumentException(String.format("Error in %d. dimension: max is lower than min (%s < %s)", t + 1, max[t], min[t]));
                }
                if(pref[t].compareTo(min[t]) < 0) pref[t] = min[t];
                else if(pref[t].compareTo(max[t]) > 0) pref[t] = max[t];
            }
            realSizeWriter.set(comp.compose(pref));
            return true;
        };

        maxSize.setterListeners().get()._getUtilListeners().add(onAnySizeChange);
        minSize.setterListeners().get()._getUtilListeners().add(onAnySizeChange);
        prefSize.setterListeners().get()._getUtilListeners().add(onAnySizeChange);
        sizeConstraint.setterListeners().get()._getUtilListeners().add(onAnySizeChange);

        prefSize.set(DEF_PREF); //proběhneme setterem, aby se inicializovala realSize a otestovala platnost invariantů
    }

    /**
     * The upper boundary for the value of <code>realSize</code>
     * */
    public final Property<Pos> maxSize;

    /**
     * The lower boundary for the value of <code>realSize</code>
     * */
    public final Property<Pos> minSize;
    /**
     * The preffered value for <code>realSize</code>
     * */
    public final Property<Pos> prefSize;

    /**
     * The optional additional upper boundary for the value of <code>realSize</code>.
     *
     * Should be set only by internal utilies of this library, not by the user,
     * unless he is really confident in what he is doing.
     * */
    public final Property<Pos> sizeConstraint;

    /**
     * Property that represents the real value that a cannonical instance of {@link DrawableElem}
     * should attain given the values of the other size Properties.
     *
     * Can be set only through modifiing the other size Properties.
     * */
    public final ReadonlyProperty<Pos> realSize;

//private:
    private Pos maxPos, minPos, prefPos;
    private final WriteonlyWrapper<Pos> realSizeWriter;
}
