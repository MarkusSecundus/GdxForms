package com.markussecundus.forms.wrappers.property.binding;

import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.function.BiFunction;
import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.utils.function.TriFunction;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;
import com.markussecundus.forms.wrappers.property.Property;




/**
 * Statická třída sdružující funkce pro bindování Properties.
 *
 * @see Property
 * @see com.markussecundus.forms.events.EventListener
 * @see com.markussecundus.forms.events.EventDelegate
 *
 * @author MarkusSecundus
 * */
public class Bindings {

    /**
     * Jednosměrně naváže hodnotu property na hodnotu jiné property.
     *
     * @param executor instance {@link BindingExecutor}u, která bude mít na starosti provedení bindingu
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojové property
     * @param source property, na níž má cílová property nově být závislá
     * @param transform funkce, která bude z hodnot zdrojové property vytahovat hodnotu, na kterou má být nastavena závislá property
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static<T1, T2> Binding<T1> bind(BindingExecutor executor, WriteonlyWrapper<T1> target, Function<T2, T1> transform, Property<T2> source){
        return bind(executor, target, arr->transform.apply((T2)(arr[0])), new Property<?>[]{source});
    }

    /**
     * Jednosměrně naváže hodnotu property na hodnoty dvojice properties.
     *
     * @param executor instance {@link BindingExecutor}u, která bude mít na starosti provedení bindingu
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojových properties
     * @param source1 property, na níž má cílová property nově být závislá
     * @param source2 property, na níž má cílová property nově být závislá
     * @param transform funkce, která bude z hodnot zdrojových properties vytahovat hodnotu, na kterou má být nastavena závislá property
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static<T1, T2, T3> Binding<T1> bind(BindingExecutor executor, WriteonlyWrapper<T1> target, BiFunction<T2, T3, T1> transform, Property<T2> source1, Property<T3> source2){
        return bind(executor, target, arr->transform.apply((T2)(arr[0]),((T3)(arr[1]))), new Property<?>[]{source1, source2});
    }


    /**
     * Jednosměrně naváže hodnotu property na hodnoty trojice properties.
     *
     * @param executor instance {@link BindingExecutor}u, která bude mít na starosti provedení bindingu
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojových properties
     * @param source1 property, na níž má cílová property nově být závislá
     * @param source2 property, na níž má cílová property nově být závislá
     * @param source3 property, na níž má cílová property nově být závislá
     * @param transform funkce, která bude z hodnot zdrojových properties vytahovat hodnotu, na kterou má být nastavena závislá property
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static<T1, T2, T3, T4> Binding<T1> bind(BindingExecutor executor, WriteonlyWrapper<T1> target, TriFunction<T2, T3, T4, T1> transform, Property<T2> source1, Property<T3> source2, Property<T4> source3){
        return bind(executor, target, arr->transform.apply((T2)(arr[0]),((T3)(arr[1])),((T4)(arr[2]))), new Property<?>[]{source1, source2, source3});
    }


    /**
     * Jednosměrně naváže hodnotu property na hodnoty libovolně velké množiny properties.
     * <p>
     * Nejobecnější, avšak typově-nebezpečná varianta.
     * <p>
     * (Jelikož Java nemá variadická generika, nenapadá mě žádný lepší způsob, jak toto naimplementovat.)
     *
     *
     * @param executor instance {@link BindingExecutor}u, která bude mít na starosti provedení bindingu
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojových properties
     * @param sources množina properties, na nichž má cílová property nově být závislá
     * @param transform funkce, která bude z hodnot zdrojových properties vytahovat hodnotu, na kterou má být nastavena závislá property
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static <T> Binding<T> bind(BindingExecutor executor, WriteonlyWrapper<T> target, Function<Object[], T> transform, Property<?>[] sources){
        Binding<T> binding = new Binding<>(target, transform, sources);

        for(Property<?> src: sources) {
            BinderListener.<T>findOrMakeIfNone(executor, src.getSetterListeners()).bindings.add(binding);
        }
        return binding;
    }






    /**
     * Jednosměrně naváže hodnotu property na hodnotu jiné property.
     * <p>
     * Jako {@link BindingExecutor} bude použit <code>{@link BindingExecutor}.DEFAULT</code>
     *
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojové property
     * @param source property, na níž má cílová property nově být závislá
     * @param transform funkce, která bude z hodnot zdrojové property vytahovat hodnotu, na kterou má být nastavena závislá property
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static<T1, T2> Binding<T1> bind(WriteonlyWrapper<T1> target, Function<T2, T1> transform, Property<T2> source){
        return bind(BindingExecutor.DEFAULT, target, transform, source);
    }
    /**
     * Jednosměrně naváže hodnotu property na hodnotu jiné property.
     * <p>
     * Jako {@link BindingExecutor} bude použit <code>{@link BindingExecutor}.DEFAULT</code>
     * <p>
     * Jako transformační funkce bude použita identita
     *
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojové property
     * @param source property, na níž má cílová property nově být závislá
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static<T> Binding<T> bind(WriteonlyWrapper<T> target, Property<? extends T> source){
        return bind( target, FormsUtil.identity(), source);
    }
    /**
     * Jednosměrně naváže hodnotu property na hodnotu jiné property.
     * <p>
     * Jako transformační funkce bude použita identita
     *
     * @param executor instance {@link BindingExecutor}u, která bude mít na starosti provedení bindingu
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojové property
     * @param source property, na níž má cílová property nově být závislá
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static<T> Binding<T> bind(BindingExecutor executor, WriteonlyWrapper<T> target, Property<? extends T> source){
        return bind(executor,  target, FormsUtil.identity(), source);
    }



    /**
     * Jednosměrně naváže hodnotu property na hodnoty dvojice properties.
     * <p>
     * Jako {@link BindingExecutor} bude použit <code>{@link BindingExecutor}.DEFAULT</code>
     *
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojových properties
     * @param source1 property, na níž má cílová property nově být závislá
     * @param source2 property, na níž má cílová property nově být závislá
     * @param transform funkce, která bude z hodnot zdrojových properties vytahovat hodnotu, na kterou má být nastavena závislá property
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static<T1,T2, T3> Binding<T1> bind(WriteonlyWrapper<T1> target, BiFunction<T2, T3, T1> transform, Property<T2> source1, Property<T3> source2){
        return bind(BindingExecutor.DEFAULT, target, transform, source1, source2);
    }
    /**
     * Jednosměrně naváže hodnotu property na hodnoty trojice properties.
     * <p>
     * Jako {@link BindingExecutor} bude použit <code>{@link BindingExecutor}.DEFAULT</code>
     *
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojových properties
     * @param source1 property, na níž má cílová property nově být závislá
     * @param source2 property, na níž má cílová property nově být závislá
     * @param source3 property, na níž má cílová property nově být závislá
     * @param transform funkce, která bude z hodnot zdrojových properties vytahovat hodnotu, na kterou má být nastavena závislá property
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static<T1, T2, T3, T4> Binding<T1> bind(WriteonlyWrapper<T1> target, TriFunction<T2, T3, T4, T1> transform, Property<T2> source1, Property<T3> source2, Property<T4> source3){
        return bind(BindingExecutor.DEFAULT, target, transform, source1, source2, source3);
    }


    /**
     * Jednosměrně naváže hodnotu property na hodnoty libovolně velké množiny properties.
     * <p>
     * Nejobecnější, avšak typově-nebezpečná varianta.
     * <p>
     * (Jelikož Java nemá variadická generika, nenapadá mě žádný lepší způsob, jak toto naimplementovat.)
     * <p>
     * Jako {@link BindingExecutor} bude použit <code>{@link BindingExecutor}.DEFAULT</code>
     *
     *
     * @param target cílová property, jejíž hodnota bude nově závislá na hodnotě zdrojových properties
     * @param sources množina properties, na nichž má cílová property nově být závislá
     * @param transform funkce, která bude z hodnot zdrojových properties vytahovat hodnotu, na kterou má být nastavena závislá property
     *
     * @return nově vytvořený {@link Binding}
     * */
    public static <T> Binding<T> bind(WriteonlyWrapper<T> target, Function<Object[], T> transform, Property<?>[] sources) {
        return bind(BindingExecutor.DEFAULT, target, transform, sources);
    }




    /**
     * Vzájemně, obousměrně naváže hodnoty dvou properties.
     *
     *
     * @param executor instance {@link BindingExecutor}u, která bude mít na starosti provedení bindingu
     * @param prop1 první z dvojice properties, jež budou vzájemně navázány
     * @param prop2 druhá z dvojice properties, jež budou vzájemně navázány
     * @param conv1to2 funkce, jež z hodnoty 1. property určí novou hodnotu pro 2. property
     * @param conv2to1 funkce, jež z hodnoty 2. property určí novou hodnotu pro 1. property
     *
     * */
    public static<T1, T2> void bindBidirectional(BindingExecutor executor, Property<T1> prop1, Property<T2> prop2, Function<T1, T2> conv1to2, Function<T2,T1> conv2to1){
        bind(executor, prop2, conv1to2, prop1);
        bind(executor, prop1, conv2to1, prop2);
    }

    /**
     * Vzájemně, obousměrně naváže hodnoty dvou properties.
     *
     *
     * @param executor instance {@link BindingExecutor}u, která bude mít na starosti provedení bindingu
     * @param prop1 první z dvojice properties, jež budou vzájemně navázány
     * @param prop2 druhá z dvojice properties, jež budou vzájemně navázány
     * @param conv funkce, jež při změně jedné property určí, jaké hodnoty má nabýt hodnota druhá
     *
     * */
    public static<T> void bindBidirectional(BindingExecutor executor, Property<T> prop1, Property<T> prop2, Function<T,T> conv){
        bindBidirectional(executor, prop1, prop2, conv, conv);
    }

    /**
     * Vzájemně, obousměrně naváže hodnoty dvou properties.
     * <p>
     * Jako transformační funkce bude použita identita.
     *
     * @param executor instance {@link BindingExecutor}u, která bude mít na starosti provedení bindingu
     * @param prop1 první z dvojice properties, jež budou vzájemně navázány
     * @param prop2 druhá z dvojice properties, jež budou vzájemně navázány
     * */
    public static<T> void bindBidirectional(BindingExecutor executor, Property<T> prop1, Property<T> prop2){
        bindBidirectional(executor, prop1, prop2, FormsUtil.identity());
    }



    /**
     * Vzájemně, obousměrně naváže hodnoty dvou properties.
     * <p>
     * Jako {@link BindingExecutor} bude použit <code>{@link BindingExecutor}.DEFAULT</code>
     *
     *
     * @param prop1 první z dvojice properties, jež budou vzájemně navázány
     * @param prop2 druhá z dvojice properties, jež budou vzájemně navázány
     * @param conv1to2 funkce, jež z hodnoty 1. property určí novou hodnotu pro 2. property
     * @param conv2to1 funkce, jež z hodnoty 2. property určí novou hodnotu pro 1. property
     *
     * */
    public static<T1, T2> void bindBidirectional(Property<T1> prop1, Property<T2> prop2, Function<T1, T2> conv1to2, Function<T2,T1> conv2to1){
        bindBidirectional(BindingExecutor.DEFAULT, prop1, prop2, conv1to2, conv2to1);
    }


    /**
     * Vzájemně, obousměrně naváže hodnoty dvou properties.
     * <p>
     * Jako {@link BindingExecutor} bude použit <code>{@link BindingExecutor}.DEFAULT</code>
     *
     *
     * @param prop1 první z dvojice properties, jež budou vzájemně navázány
     * @param prop2 druhá z dvojice properties, jež budou vzájemně navázány
     * @param conv funkce, jež při změně jedné property určí, jaké hodnoty má nabýt hodnota druhá
     *
     * */
    public static<T> void bindBidirectional( Property<T> prop1, Property<T> prop2, Function<T,T> conv){
        bindBidirectional(BindingExecutor.DEFAULT, prop1, prop2, conv);
    }


    /**
     * Vzájemně, obousměrně naváže hodnoty dvou properties.
     * <p>
     * Jako transformační funkce bude použita identita.
     * <p>
     * Jako {@link BindingExecutor} bude použit <code>{@link BindingExecutor}.DEFAULT</code>
     *
     *
     * @param prop1 první z dvojice properties, jež budou vzájemně navázány
     * @param prop2 druhá z dvojice properties, jež budou vzájemně navázány
     * */
    public static<T> void bindBidirectional(Property<T> prop1, Property<T> prop2){
        bindBidirectional(BindingExecutor.DEFAULT, prop1, prop2);
    }


}
