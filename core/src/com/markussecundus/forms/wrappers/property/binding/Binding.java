package com.markussecundus.forms.wrappers.property.binding;

import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;


/**
 * Datová třída nesoucí informace o jedné hraně v bindovacím 'orientovaném hypergrafu'.
 *
 * @see Bindings
 * @see BinderListener
 * @see BindingExecutor
 *
 * @author MarkusSecundus
 * */
public  class Binding<T>{

    /**
     * Vytvoří novou instanci nad odpovídajícími hodnotami.
     *
     * @param sources Množina properties, na nichž závisí cílová property.
     * @param target Cílová property, do které vede bindovací hrana.
     * @param transform  Funkce, která podle hodnot zdrojových properties vypočítá hodnotu, které má nabýt cílová property.
     * */
    public Binding(WriteonlyWrapper<T> target, Function<Object[], T> transform, ReadonlyProperty<?>[] sources){
        this.sources = sources;
        this.target = target;
        this.transform = transform;
    }

    /**
     * Množina properties, na nichž závisí cílová property.
     * */
    public final ReadonlyProperty<?>[] sources;

    /**
     * Cílová property, do které vede bindovací hrana.
     * */
    public final WriteonlyWrapper<T> target;

    /**
     * Funkce, která podle hodnot zdrojových properties vypočítá hodnotu, které má nabýt cílová property.
     * */
    public final Function<Object[], T> transform;

    /**
     * Nastaví hodnotu cílové property na hodnotu vypočítanou pomocí transformační funkce z přijatých argumentů.
     *
     * @param args hodnoty odpovídající jednotlivým zdrojovým properties bindovací hrany.
     * */
    public T setValueOfTargetProperty(Object[] args){
        return target.set(transform.apply(args));
    }

}