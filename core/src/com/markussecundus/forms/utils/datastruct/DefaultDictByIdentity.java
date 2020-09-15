package com.markussecundus.forms.utils.datastruct;

import com.markussecundus.forms.utils.function.BiConsumer;
import com.markussecundus.forms.utils.function.Function;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Varianta {@link DefaultDict} zaručující, že místo rovnosti objektů budou
 * klíče rozlišovány dle rovnosti referencí.
 *
 * @param <K> typ pro hodnoty klíčů
 * @param <V> typ pro hodnoty uložené v Dictionary
 *
 * @see DefaultDict
 *
 * @author MarkusSecundus
 * */
public class DefaultDictByIdentity<K,V> extends DefaultDict<K,V> {
    /**
     * Vytvoří instanci wrapperu nad novou, prázdnou instancí {@link IdentityHashMap} a používající daný generátor
     * výchozích hodnot.
     *
     * @param supplier Generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
     * @param keyTransform Funkce, která podle tázaného klíče vygeneruje skutečný klíč, pod kterým bude uložena vegenerovaná hodnota.
     *                     Nová hodnota musí být ekvivalentní s hodnotou původní podle funkce <code>Objects.equals</code>.
     *                     Identita pokud není udána, nebo je <code>null</code>.
     * @param afterValueCreated Funkce, která se provede hned poté, co byla nová výchozí hodnota vygenerována a uložena.
     *                          Jako parametry bere vygenerovanou hodnotu a klíč, pod kterým byla uložena.
     *                          Může být <code>null</code> či neuvedena - pak se nic provádět nebude.
     * */
    public DefaultDictByIdentity(Function<K, V> supplier, Function<K, K> keyTransform, BiConsumer<K, V> afterValueCreated) {
        super(DEF_MAP_INSTANCE__FACTORY(), supplier, keyTransform, afterValueCreated);
    }

    /**
     * Vytvoří instanci wrapperu nad novou, prázdnou instancí {@link IdentityHashMap} a používající daný generátor
     * výchozích hodnot.
     *
     * @param supplier Generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
     * @param keyTransform Funkce, která podle tázaného klíče vygeneruje skutečný klíč, pod kterým bude uložena vegenerovaná hodnota.
     *                     Nová hodnota musí být ekvivalentní s hodnotou původní podle funkce <code>Objects.equals</code>.
     *                     Identita pokud není udána, nebo je <code>null</code>.
     *
     * */
    public DefaultDictByIdentity(Function<K, V> supplier, Function<K, K> keyTransform) {
        super(DEF_MAP_INSTANCE__FACTORY(), supplier, keyTransform);
    }
    /**
     * Vytvoří instanci wrapperu nad novou, prázdnou instancí {@link IdentityHashMap} a používající daný generátor
     * výchozích hodnot.
     *
     * @param supplier generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
     * */
    public DefaultDictByIdentity(Function<K, V> supplier) {
        super(DEF_MAP_INSTANCE__FACTORY(), supplier);
    }

    /**
     * @return nová instance {@link Map}, používající porovnávání dle rovnosti instancí,
     * nad kterou je defaultně {@link DefaultDictByIdentity} postaven.
     * */
    public static<K,V> Map<K,V> DEF_MAP_INSTANCE__FACTORY(){return new IdentityHashMap<>(); }



    /**
     * Varianta, kde klíče s hodnotou <code>null</code> budou považovány za klíče bez hodnoty a tedy
     * pro ně bude při vyžádání generována nová hodnota.
     *
     * @see DefaultDict
     *
     * @author MarkusSecundus
     * */
    public static class NotNullValue<K,V> extends DefaultDictByIdentity<K,V>{


        @Override
        protected boolean valueNotPresentCondition(Object key, V returnedValue) {
            return returnedValue != null;
        }


        /**
         * Vytvoří instanci wrapperu nad novou, prázdnou instancí {@link Map} a používající daný generátor
         * výchozích hodnot.
         *
         * @param supplier Generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
         * @param keyTransform Funkce, která podle tázaného klíče vygeneruje skutečný klíč, pod kterým bude uložena vegenerovaná hodnota.
         *                     Nová hodnota musí být ekvivalentní s hodnotou původní podle funkce <code>Objects.equals</code>.
         *                     Identita pokud není udána, nebo je <code>null</code>.
         * @param afterValueCreated Funkce, která se provede hned poté, co byla nová výchozí hodnota vygenerována a uložena.
         *                          Jako parametry bere vygenerovanou hodnotu a klíč, pod kterým byla uložena.
         *                          Může být <code>null</code> či neuvedena - pak se nic provádět nebude.
         * */
        public NotNullValue(com.markussecundus.forms.utils.function.Function<K,V> supplier,
                            com.markussecundus.forms.utils.function.Function<K,K> keyTransform,
                            com.markussecundus.forms.utils.function.BiConsumer<K,V> afterValueCreated){
            super(supplier, keyTransform, afterValueCreated);
        }


        /**
         * Vytvoří instanci wrapperu nad novou, prázdnou instancí {@link Map} a používající daný generátor
         * výchozích hodnot.
         *
         * @param supplier Generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
         * @param keyTransform Funkce, která podle tázaného klíče vygeneruje skutečný klíč, pod kterým bude uložena vegenerovaná hodnota.
         *                     Nová hodnota musí být ekvivalentní s hodnotou původní podle funkce <code>Objects.equals</code>.
         *                     Identita pokud není udána, nebo je <code>null</code>.
         *
         * */
        public NotNullValue(com.markussecundus.forms.utils.function.Function<K,V> supplier,
                            com.markussecundus.forms.utils.function.Function<K,K> keyTransform){
            super(supplier, keyTransform);
        }

        /**
         * Vytvoří instanci wrapperu nad novou, prázdnou instancí {@link Map} a používající daný generátor
         * výchozích hodnot.
         *
         * @param supplier generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
         * */
        public NotNullValue(com.markussecundus.forms.utils.function.Function<K,V> supplier){super( supplier);}

    }


/*
    public DefaultDictByIdentity(DefaultDict<FormsUtil.WrapperForReferenceComparison<K>, V> base){
        this.base = base;
    }

    public DefaultDictByIdentity(Function<K,V> supplier){
        this(new DefaultDict<>(key->supplier.apply(key.item), FormsUtil.WrapperForReferenceComparison::cpy));
    }

    public final DefaultDict<FormsUtil.WrapperForReferenceComparison<K>, V> base;

    private final FormsUtil.WrapperForReferenceComparison<K> _instanceFinder = new FormsUtil.WrapperForReferenceComparison<>(null);
    private FormsUtil.WrapperForReferenceComparison<K> instanceFinder(){return _instanceFinder;}
    private FormsUtil.WrapperForReferenceComparison<Object> instanceFinderObj(){return (FormsUtil.WrapperForReferenceComparison) _instanceFinder;}


    @Override
    public int size() {
        return base.size();
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return base.containsKey(instanceFinderObj().with(o));
    }

    @Override
    public boolean containsValue(Object o) {
        return base.containsValue(o);
    }

    public V get(Object key){
        return base.get(instanceFinderObj().with(key));
    }

    @Override
    public V put(K k, V v) {
        return base.put(new FormsUtil.WrapperForReferenceComparison<>(k), v);
    }

    @Override
    public V remove(Object o) {
        return base.remove(instanceFinderObj().with(o));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for(Map.Entry<? extends K, ? extends V> v: map.entrySet())
            put(v.getKey(), v.getValue());
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public Set<K> keySet() {
        return new RedirectedSet<>(base.keySet(), k->k.item, FormsUtil.WrapperForReferenceComparison::new);
    }

    @Override
    public Collection<V> values() {
        return base.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new RedirectedSet<>(base.entrySet(),
                e->new MapEntryWithRedirectedKey<>(e, k->k.item),
                e->new MapEntryWithRedirectedKey<>(e, FormsUtil.WrapperForReferenceComparison::new));
    }

*/
}
