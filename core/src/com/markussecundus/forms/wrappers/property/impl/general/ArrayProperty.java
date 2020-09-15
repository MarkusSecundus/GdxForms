package com.markussecundus.forms.wrappers.property.impl.general;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.utils.datastruct.DefaultDict;
import com.markussecundus.forms.wrappers.property.Property;

import java.util.Arrays;
import java.util.Map;


/**
 * Objekt sdružující dohromady pole Properties mapovaných na pole jim odpovídajících hodnot.
 * <p>
 * Poskytuje i hromadné listenery vztahující se na všechny properties v poli.
 *
 * @param <T> typ, jehož {@link Property}ies v poli nabývají
 *
 * @see Property
 * @see SimpleProperty
 *
 * @see com.markussecundus.forms.elements.impl.layouts.BasicLinearLayout
 *
 * */
public class ArrayProperty<T> {
//public:

    /**
     * Vytvoří pole Properties mapovaných na prvky vstupního pole. K prvkům pole by dále mělo být
     * přistupováno jedině skrze tyto Properties.
     *
     * @param elems hodnoty pro vytvářené Properties
     * */
    public ArrayProperty(T[] elems){
        this.elems = elems;
        properties = new Property[elems.length];
        for(int t=0;t<elems.length;++t) {
            final int i = t;
            properties[i] = new AbstractProperty<T>() {
                protected T obtain() { return elems[i]; }
                protected T change(T val) { return elems[i] = val; }
            };
        }


        massSetterDelegates = new DefaultDict<>(priority->{
            EventDelegate<Property.SetterListenerArgs<T>> delegate = EventDelegate.make();

            for(Property<T> prop: properties)
                prop.getSetterListeners().getListeners(priority).add(delegate);

            return delegate;
        });

        massGetterDelegates = new DefaultDict<>(priority->{
            EventDelegate<Property.GetterListenerArgs<T>> delegate = EventDelegate.make();

            for(Property<T> prop: properties)
                prop.getGetterListeners().getListeners(priority).add(delegate);

            return delegate;
        });
    }

    /**
     * @return i-tá Property
     * */
    public Property<T> at(int i){return properties[i];}

    /**
     * Obvolá gettery všech Properties a vrátí jejich hodnoty v poli
     *
     * @return nová kopie pole obsahujícího hodnoty všech Properties
     * */
    public T[] getArray(){
        for(Property<T> prop: properties)
            prop.get();
        return Arrays.copyOf(elems, elems.length);
    }


    /**
     * Delegát, jehož listenery budou vykonávány v <code>Listener(priority)</code>u setteru každé z Properties v poli.
     *
     * @param priority priorita, pod kterou je delegát uložen
     *
     * @return Delegát, jehož listenery budou vykonávány v <code>Listener(priority)</code>u setteru každé z Properties v poli.
     * */
    public EventDelegate<Property.SetterListenerArgs<T>> massSetterDelegate(Integer priority){
        return massSetterDelegates.get(priority);
    }

    /**
     * Delegát, jehož listenery budou vykonávány v <code>Listener(priority)</code>u getteru každé z Properties v poli.
     *
     * @param priority priorita, pod kterou je delegát uložen
     *
     * @return Delegát, jehož listenery budou vykonávány v <code>Listener(priority)</code>u getteru každé z Properties v poli.
     * */
    public EventDelegate<Property.GetterListenerArgs<T>> massGetterDelegate(Integer priority){
        return massGetterDelegates.get(priority);
    }


//private:
    private final Property<T>[] properties;
    private final T[] elems;

    private final Map<Integer, EventDelegate<Property.SetterListenerArgs<T>>> massSetterDelegates;
    private final Map<Integer, EventDelegate<Property.GetterListenerArgs<T>>> massGetterDelegates;
}
