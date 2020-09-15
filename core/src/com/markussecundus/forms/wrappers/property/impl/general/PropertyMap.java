package com.markussecundus.forms.wrappers.property.impl.general;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.utils.datastruct.DefaultDict;
import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.wrappers.property.Property;

import java.util.HashMap;
import java.util.Map;

/**
 * Objekt sdružující dohromady množinu Properties mapovaných na klíče.
 *<p>
 * Poskytuje i hromadné listenery vztahující se na všechny obsažené properties najednou.
 *
 *
 * @param <T> typ, jehož {@link Property}ies v množině nabývají
 * @param <K> typ, na nějž jsou {@link Property}ies mapovány
 *
 * @see Property
 * @see SimpleProperty
 *
 * @see com.markussecundus.forms.elements.impl.layouts.BasicLinearLayout
 *
 * */
public class PropertyMap<K, T> {
//public:

    /**
     * Vytvoří novou instanci {@link PropertyMap} nad novou instancí {@link HashMap},
     * používající dodanou funkci pro generaci případných dalších {@link Property}ies.
     *
     * @param defaultValSupplier generátor nových properties pro nově vyžádané klíče
     * */
    public PropertyMap(Function<K, Property<T>> defaultValSupplier){
        this(new HashMap<>(), defaultValSupplier);
    }

    /**
     * Vytvoří novou instanci odkazující na properties v dodané {@link Map}ě,
     * používající dodanou funkci pro generaci případných dalších {@link Property}ies.
     *
     * @param defaultValSupplier generátor nových properties pro nově vyžádané klíče
     * @param elems mapa properties, nad kterou bude nově vytvářený wrapper odkazovat
     * */
    public PropertyMap(Map<K, Property<T>> elems, Function<K,Property<T>> defaultValSupplier){
        properties = new DefaultDict<>(elems, defaultValSupplier, null, this::onNewPropertyAdded);

        massSetterDelegates = new DefaultDict<>(priority ->{
            EventDelegate<Property.SetterListenerArgs<T>> delegate = EventDelegate.make();

            for(Property<T> prop:properties.values())
                prop.getSetterListeners().getListeners(priority).add(delegate);

            return delegate;
        });

        massGetterDelegates = new DefaultDict<>(priority->{
            EventDelegate<Property.GetterListenerArgs<T>> delegate = EventDelegate.make();

            for(Property<T> prop:properties.values())
                prop.getGetterListeners().getListeners(priority).add(delegate);

            return delegate;
        });
    }

    private void onNewPropertyAdded(K key, Property<T> prop){
        for(Integer priority: massSetterDelegates.keySet())
            prop.getSetterListeners().getListeners(priority).add(massSetterDelegates.get(priority));

        for(Integer priority: massGetterDelegates.keySet())
            prop.getGetterListeners().getListeners(priority).add(massGetterDelegates.get(priority));
    }

    /**
     * Vrátí property odpovídající danému klíči. Pokud taková neexistuje, vytvoří novou.
     *
     * @return property nalezená/vytvořená pro daný klíč
     * */
    public Property<T> get(K key){return properties.get(key);}


    /**
     * Delegát, jehož listenery budou vykonávány v <code>Listener(priority)</code>u setteru každé z Properties v mapě.
     *
     * @param priority priorita, pod kterou je delegát uložen
     *
     * @return Delegát, jehož listenery budou vykonávány v <code>Listener(priority)</code>u setteru každé z Properties v mapě.
     * */
    public EventDelegate<Property.SetterListenerArgs<T>> massSetterDelegate(Integer priority){
        return massSetterDelegates.get(priority);
    }

    /**
     * Delegát, jehož listenery budou vykonávány v <code>Listener(priority)</code>u getteru každé z Properties v mapě.
     *
     * @param priority priorita, pod kterou je delegát uložen
     *
     * @return Delegát, jehož listenery budou vykonávány v <code>Listener(priority)</code>u getteru každé z Properties v mapě.
     * */
    public EventDelegate<Property.GetterListenerArgs<T>> massGetterDelegate(Integer priority){
        return massGetterDelegates.get(priority);
    }


//private:
    private final Map<K,Property<T>> properties;

    private final Map<Integer, EventDelegate<Property.SetterListenerArgs<T>>> massSetterDelegates;
    private final Map<Integer, EventDelegate<Property.GetterListenerArgs<T>>> massGetterDelegates;
}
