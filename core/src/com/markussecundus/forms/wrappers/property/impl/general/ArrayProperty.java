package com.markussecundus.forms.wrappers.property.impl.general;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.wrappers.property.Property;

import java.util.Arrays;


/**
 * Objekt sdružující dohromady pole Properties mapovaných na pole jim odpovídajících hodnot.
 *
 * Poskytuje i hromadné listenery vztahující se na všechny properties v poli.
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
     * @return Delegát, jehož listenery budou vykonávány v <code>_utilListener</code>u setteru každé z Properties v poli.
     * */
    public EventDelegate<Property.SetterListenerArgs<T>> _massSetterUtilDelegate(){
        if(_massSetterUtilDelegate ==null){
            _massSetterUtilDelegate = EventDelegate.make();
            for(Property<T> prop: properties)
                prop.getSetterListeners()._getUtilListeners().add(_massSetterUtilDelegate);
        }
        else for(Property<T> prop: properties)
            prop.getSetterListeners();
        return _massSetterUtilDelegate;
    }

    /**
     * @return Delegát, jehož listenery budou vykonávány v <code>listener</code>u setteru každé z Properties v poli.
     * */
    public EventDelegate<Property.SetterListenerArgs<T>> _massSetterDelegate(){
        if(_massSetterDelegate ==null){
            _massSetterDelegate = EventDelegate.make();
            for(Property<T> prop: properties)
                prop.getSetterListeners().getListeners().add(_massSetterDelegate);
        }
        else for(Property<T> prop: properties)
            prop.getSetterListeners();
        return _massSetterDelegate;
    }


    /**
     * @return Delegát, jehož listenery budou vykonávány v <code>_utilListener</code>u getteru každé z Properties v poli.
     * */
    public EventDelegate<Property.GetterListenerArgs<T>> _massGetterUtilDelegate(){
        if(_massGetterUtilDelegate == null){
            _massGetterUtilDelegate = EventDelegate.make();
            for(Property<T> prop: properties)
                prop.getGetterListeners()._getUtilListeners().add(_massGetterUtilDelegate);
        }
        else for(Property<T> prop: properties)
            prop.getGetterListeners();
        return _massGetterUtilDelegate;
    }

    /**
     * @return Delegát, jehož listenery budou vykonávány v <code>listener</code>u getteru každé z Properties v poli.
     * */
    public EventDelegate<Property.GetterListenerArgs<T>> _massGetterDelegate(){
        if(_massGetterDelegate ==null){
            _massGetterDelegate = EventDelegate.make();
            for(Property<T> prop: properties)
                prop.getGetterListeners().getListeners().add(_massGetterDelegate);
        }
        else for(Property<T> prop: properties)
            prop.getGetterListeners();
        return _massGetterDelegate;
    }

//private:
    private final Property<T>[] properties;
    private final T[] elems;

    private EventDelegate<Property.SetterListenerArgs<T>> _massSetterUtilDelegate = null;
    private EventDelegate<Property.SetterListenerArgs<T>> _massSetterDelegate = null;
    private EventDelegate<Property.GetterListenerArgs<T>> _massGetterUtilDelegate = null;
    private EventDelegate<Property.GetterListenerArgs<T>> _massGetterDelegate = null;
}
