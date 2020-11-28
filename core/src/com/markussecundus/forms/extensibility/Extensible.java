package com.markussecundus.forms.extensibility;

import com.markussecundus.forms.utils.datastruct.DefaultDictByIdentity;
import com.markussecundus.forms.utils.function.Function;

import java.util.HashMap;
import java.util.Map;


/**
 * Interface, through which extension objects (e.g. data containers for mixins implemented as a default method interface, etc.) can be plugged to an object.
 *
 * @see IExtensible
 *
 * @author MarkusSecundus
 * */
public interface Extensible {

    /**
     * @return table of extensions mapped to their {@link Class} objects
     * */
    public Map<Class<?>,Object> getExtensionsMap();

    /**
     * Ensures that the object being extended has the given extension (eventually creates the extension)
     * and returns the extension object.
     *
     * @param <T> type of the sought extension
     * @param cl class to which the extension object is mapped
     * @param provider function to create the extension object, if it doesn't already exist; takes the object to be extended as an argument
     *
     * @return extension object, if it already exists, eventually its newly created and added instance
     * */
    public default<T> T getExtension(Class<? super T> cl, Function<Extensible, T> provider){
        Map<Class<?>, Object> extensions = getExtensionsMap();
        Object extension = extensions.get(cl);

        if(extension == null){
            extension = provider.apply(this);
            extensions.put(cl, extension);
        }
        return (T) extension;
    }




    /**
     * Primitive, naive implementation, that, being an <code>interface</code> doesn't have to be composed into a class hierarchy,
     * but it achieves it by storing all the extension maps for particular objects in a global table - which naturally
     * can not be cleared at any time during the whole run of the programme, which naturally results in a
     * big memory-leak (objects, that are referenced in it will never be garbage-collected,
     * even if they are not referenced from any other place in the programme...).
     * <p></p>
     * Should never be used by any circumstances - it is intended more to present an, on first sight
     * quite tempting, but actually really dangerous antipattern.
     *
     * @see IExtensible
     * @see Extensible
     *
     * @author MarkusSecundus
     * */
    public static interface ImplementationThroughMemoryLeak extends Extensible{

        /**
         * {@inheritDoc}
         *
         * Finds, evntl. creates and finds an extension {@link Map} in a global table indexed by
         * the provided object reference.
         * */
        @Override
        default Map<Class<?>, Object> getExtensionsMap(){
            return Util.extensions.get(this);
        }

        /**
         * Class encapsulating private static fields of the {@link ImplementationThroughMemoryLeak} interface
         * (because Java for some reason doesn't allow private fields directly on an interface).
         *
         * @see ImplementationThroughMemoryLeak
         *
         * @author MarkusSecundus
         * */
        public static class Util{
            private static final Map<ImplementationThroughMemoryLeak, Map<Class<?>, Object>> extensions = new DefaultDictByIdentity<>(self->new HashMap<>());
        }
    }
}
