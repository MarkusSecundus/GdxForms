package com.markussecundus.forms.elements.impl.utils;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.utils.function.Function;

import java.util.HashMap;
import java.util.Map;

/**
 * A lightweight utility that encapsulates the functionality of referencing {@link com.markussecundus.forms.elements.Element}s
 * of a {@link com.markussecundus.forms.elements.UberElement} by random tags somehow associated with them.
 *
 * @param <Elem> type of the stored elements
 *
 * @see com.markussecundus.forms.elements.UberElement
 * @see com.markussecundus.forms.elements.UberDrawable
 *
 * @author MarkusSecunds
 * */
public class ElementsTagMap<Elem> {

    /**
     * The Dictionary object used to store the tags and their associated {@link com.markussecundus.forms.elements.Element}s.
     * Directly accesible for the user to exploit.
     * */
    public final Map<Object, Elem> map = new HashMap<>();

    /**
     * Convenient shortcut for getting Elements from the <code>map</code> by tag.
     *
     * Throws {@link RuntimeException} when nothing found to make debugging easier.
     * */
    public Elem get(Object tag){
        Elem ret = map.get(tag);
        if(ret==null)
            throw new RuntimeException(String.format("No value found for tag: '%s'", tag));
        return ret;
    }

    /**
     * Adds listeners to Element adder and deleter delegates, so that a tag will be
     * associated automatically for each element when it is added and removed
     * when the associated element is removed.
     *
     * @param adderListener listener called on each element newly added to a collection
     * @param deleterListener listener called on each element deleted from a collection
     * @param tagGetter function that generates the tag associated with given <code>Elem</code> instance
     * */
    public void bindTag(EventDelegate<Elem> adderListener, EventDelegate<?> deleterListener, Function<Elem, Object> tagGetter){
        EventListener<Elem> adder = e->{
            map.put(tagGetter.apply(e), e);
            return true;
        };
        EventListener<Object> deleter = o->{
            try{
                map.remove(tagGetter.apply((Elem)o));
            }catch(Exception ignored){}
            return true;
        };
        adderListener._getUtilListeners().add(adder);
        deleterListener._getUtilListeners().add(deleter);
    }
}
