package com.markussecundus.forms.elements.impl.utils;

import com.markussecundus.forms.elements.DrawableElem;
import com.markussecundus.forms.elements.Element;
import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.utils.datastruct.ObservedList;
import com.markussecundus.forms.utils.datastruct.UniqueList;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


/**
 * Utility that provides basic functionality for containing children of an {@link com.markussecundus.forms.elements.UberDrawable}.
 *
 * Supposed use is to make an instance of {@link com.markussecundus.forms.elements.UberDrawable} own
 * a private instance of {@link ElementLists} and redirect the calls for its children lists to it.
 *
 * Makes sure that all the children present in the list of <code>drawableChildren</code> are also
 * present in the list of <code>children</code> and so on. Also provides listeners to inject
 * additional functionality for the events of a child being added, removed, usw.
 *
 * @param <Rend> The renderer type used by contained {@link DrawableElem}s
 * @param <Pos> Vector type used by contained {@link DrawableElem}s
 *
 * @see com.markussecundus.forms.elements.UberDrawable
 * @see com.markussecundus.forms.elements.impl.layouts.BasicAbstractLayout
 * @see ObservedList
 *
 * @author MarkusSecundus
 * */
public abstract class ElementLists<Rend, Pos> {
//public:

    /**
     * The list containing all the {@link Element}s, that belong to the owner {@link com.markussecundus.forms.elements.UberDrawable}.
     *
     * Calls the <code>onElementAdded</code> method for every new element added,
     * <code>onElementRemoved</code> for every element removed or set to a different value.
     *
     * Makes sure that it contains all the elements in <code>drawables</code> and that
     * <code>drawables</code> do not contain anything that is not present here.
     *
     * If an element of <code>drawables</code> is set to a different value through this list,
     * it will always be removed from <code>drawables</code> without the substitute being added
     * there, to insure type safety.
     *
     * @see ObservedList
     * */
    public final ObservedList<Element> elements = new ObservedList<Element>(_gen_elem_list_type()) {

        @Override protected void onAdded(Element element, int index) {
            onElementAdded(element);
        }

        @Override protected void onDelete(Object t) {
            onElementRemoved(t);
            drawableElems.base.remove(t);
        }

        @Override protected void onSet(Element oldElem, Element newElem, int index) {
            onElementRemoved(oldElem);
            drawableElems.base.remove(oldElem);
            onElementAdded(newElem);
        }

        @Override protected void onClear() {
            for(Element v:this.base)
                onElementRemoved(v);
            drawableElems.base.clear();
        }

        @Override protected void onRemoveAll(Collection<?> col) {
            for(Element v:this.base)
                if(col.contains(v))
                    onElementRemoved(v);
            drawableElems.base.removeAll(col);
        }
    };

    /**
     * The list containing all the {@link Element}s, that belong to the owner {@link com.markussecundus.forms.elements.UberDrawable}
     * that implement the {@link DrawableElem} interface ant that are explicitly added here to be drawn on the screen.
     *
     * An {@link Element} can implement {@link DrawableElem} and be not present here if it was explicitly
     * added only to <code>children</code>.
     *
     * Calls the <code>onDrawableAdded</code> method for every new Drawable added,
     * <code>onElementRemoved</code> for every Drawable removed or set to a different value.
     *
     * Makes sure that it contains all the elements in <code>drawables</code> and that
     * <code>drawables</code> do not contain anything that is not present here.
     *
     * @see ObservedList
     * */
    public final ObservedList<DrawableElem<Rend, Pos>> drawableElems = new ObservedList<DrawableElem<Rend, Pos>>(_gen_draw_list_type()) {
        @Override protected void onAdded(DrawableElem<Rend, Pos> rendPosDrawable, int index) {
            elements.base.add(rendPosDrawable);
            onDrawableAdded(rendPosDrawable);
        }

        @Override protected void onDelete(Object t) {
            onElementRemoved(t);
            elements.base.remove(t);
        }

        @Override protected void onSet(DrawableElem<Rend, Pos> oldElem, DrawableElem<Rend, Pos> newElem, int index) {
            onElementRemoved(oldElem);
            int i = elements.base.indexOf(oldElem);
            if(i>=0)
                elements.base.set(i, newElem);
            onDrawableAdded(newElem);
        }

        @Override protected void onClear() {
            for(Element v:this.base)
                onElementRemoved(v);
            elements.base.removeAll(this.base);
        }

        @Override protected void onRemoveAll(Collection<?> col) {
            for(Element v:this.base)
                if(col.contains(v))
                    onElementRemoved(v);
            elements.base.removeAll(col);
        }
    };


//protected:
    /**
     * Called when a new {@link Element} is added to the <code>elements</code> List.
     *
     * @param e the newly added element
     * */
    protected abstract void onElementAdded(Element e);
    /**
     * Called on any attempt to remove an object from <code>elements</code> or <code>drawables</code>.
     *
     * @param o the object attempted to remove
     * */
    protected abstract void onElementRemoved(Object o);

    /**
     * Called when a new {@link DrawableElem} is added to the <code>drawables</code> List.
     *
     * @param drw the newly added drawable
     * */
    protected abstract void onDrawableAdded(DrawableElem<Rend, Pos> drw);


    /**
     * Generates the instance of {@link List} to be used for <code>elements</code> field.
     *
     * By default creates <code>new UniqueList<>(new ArrayList<>(), new HashSet<>())</code>.
     *
     * @see UniqueList
     * @see ArrayList
     * @see HashSet
     * */
    protected <T> List<T> _gen_elem_list_type(){return new UniqueList<>(new ArrayList<>(), new HashSet<>());}

    /**
     * Generates the instance of {@link List} to be used for <code>drawables</code> field.
     *
     * By default directs to <code>_gen_elem_list_type</code>.
     * */
    protected <T> List<T> _gen_draw_list_type(){return _gen_elem_list_type();}





//public:

    /**
     * {@inheritDoc}
     *
     * Variant of {@link ElementLists} that provides additional {@link ConstProperty}s
     * encapsulating the Lists.
     *
     *
     * @param <Rend> The renderer type used by contained {@link DrawableElem}s
     * @param <Pos> Vector type used by contained {@link DrawableElem}s
     *
     * @see ElementLists
     *
     * @author MarkusSecundus
     * */
    public static abstract class WithProperties<Rend, Pos> extends ElementLists<Rend, Pos>{

        /**
         * The list containing all the {@link Element}s, that belong to the owner {@link com.markussecundus.forms.elements.UberDrawable}.
         *
         * Calls the <code>onElementAdded</code> method for every new element added,
         * <code>onElementRemoved</code> for every element removed or set to a different value.
         *
         * Makes sure that it contains all the elements in <code>drawables</code> and that
         * <code>drawables</code> do not contain anything that is not present here.
         *
         * If an element of <code>drawables</code> is set to a different value through this list,
         * it will always be removed from <code>drawables</code> without the substitute being added
         * there, to insure type safety.
         *
         * @see Element
         * @see ObservedList
         * @see ConstProperty
         * */
        public final ConstProperty<List<Element>> elementsAsProperty = new AbstractConstProperty<List<Element>>() {
            protected List<Element> obtain() { return elements; }
        };

        /**
         * The list containing all the {@link Element}s, that belong to the owner {@link com.markussecundus.forms.elements.UberDrawable}
         * that implement the {@link DrawableElem} interface ant that are explicitly added here to be drawn on the screen.
         *
         * An {@link Element} can implement {@link DrawableElem} and be not present here if it was explicitly
         * added only to <code>children</code>.
         *
         * Calls the <code>onDrawableAdded</code> method for every new Drawable added,
         * <code>onElementRemoved</code> for every Drawable removed or set to a different value.
         *
         * Makes sure that it contains all the elements in <code>drawables</code> and that
         * <code>drawables</code> do not contain anything that is not present here.
         *
         * @see DrawableElem
         * @see ObservedList
         * @see ConstProperty
         * */
        public final ConstProperty<List<DrawableElem<Rend, Pos>>> drawablesAsProperty = new AbstractConstProperty<List<DrawableElem<Rend, Pos>>>() {
            protected List<DrawableElem<Rend, Pos>> obtain() { return drawableElems; }
        };
    }


    /**
     * Variant of {@link ElementLists} that provides additional {@link ConstProperty}s
     *  encapsulating the Lists and implements the <code>onElementAdded</code>,
     *  <code>onElementRemoved</code> and <code>onDrawableAdded</code> abstract
     *  methods by redirecting them to corresponding newly added {@link EventDelegate}s.
     *
     * */
    public static class Delegated<Rend, Pos> extends WithProperties<Rend, Pos>{

        /**
         * Called when a new {@link Element} is added to the <code>elements</code> List.
         *
         * Provided with the newly added element as argument.
         * */
        public final EventDelegate<Element> onElementAdded = EventDelegate.make();

        /**
         * Called on any attempt to remove an object from <code>elements</code> or <code>drawables</code>.
         *
         * Provided with the object attempted to remove as argument.
         * */
        public final EventDelegate<Object> onElementRemoved = EventDelegate.make();

        /**
         * Called when a new {@link DrawableElem} is added to the <code>drawables</code> List.
         *
         * Provided with the newly added drawable as argument.
         * */
        public final EventDelegate<DrawableElem<Rend, Pos>> onDrawableAdded = EventDelegate.make();

        /**
         * Executes the <code>onElementAdded</code> listener.
         *
         * {@inheritDoc}
         * */
        protected void onElementAdded(Element e) { onElementAdded.exec(e); }

        /**
         * Executes the <code>onElementRemoved</code> listener.
         *
         * {@inheritDoc}
         * */
        protected void onElementRemoved(Object o) { onElementRemoved.exec(o); }

        /**
         * Executes the <code>onDrawableAdded</code> listener.
         *
         * {@inheritDoc}
         * */
        protected void onDrawableAdded(DrawableElem<Rend, Pos> drw) { onDrawableAdded.exec(drw); }
    }
}
