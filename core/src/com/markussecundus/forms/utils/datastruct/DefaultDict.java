package com.markussecundus.forms.utils.datastruct;

import com.markussecundus.forms.utils.FormsUtil;

import java.util.function.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Mírně obecnější obdoba Pythoního <code>collections.defaultdict</code>.
 *
 * Wrapper nad libovolným {@link Map}. Navíc má určen generátor výchozích hodnot.
 * Pokud by pro nějaký klíč měla vrátit hodnotu <code>nulll</code>, bude místo
 * toho pro daný klíč vygenerována nová hodnota pomocí této funkce.
 *
 * @param <K> typ pro hodnoty klíčů
 * @param <V> typ pro hodnoty uložené v Dictionary
 *
 * @author MarkusSecundus
 * */
public class DefaultDict<K,V> implements Map<K,V> {

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
    public DefaultDict(com.markussecundus.forms.utils.function.Function<K,V> supplier,
                       com.markussecundus.forms.utils.function.Function<K,K> keyTransform,
                       com.markussecundus.forms.utils.function.BiConsumer<K,V> afterValueCreated){
        this(DEF_MAP_INSTANCE_FACTORY(), supplier, keyTransform, afterValueCreated);
    }

    /**
     * Vytvoří instanci wrapperu nad daným objektem {@link Map} a používající daný generátor
     * výchozích hodnot.
     *
     * @param base instance {@link Map}, nad kterou bude tento objekt fungovat jako wrapperu
     * @param supplier generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
     * @param keyTransform Funkce, která podle tázaného klíče vygeneruje skutečný klíč, pod kterým bude uložena vegenerovaná hodnota.
     *                     Nová hodnota musí být ekvivalentní s hodnotou původní podle funkce <code>Objects.equals</code>.
     *                     Identita pokud není udána, nebo je <code>null</code>.
     * @param afterValueCreated Funkce, která se provede hned poté, co byla nová výchozí hodnota vygenerována a uložena.
     *                          Jako parametry bere vygenerovanou hodnotu a klíč, pod kterým byla uložena.
     *                          Může být <code>null</code> či neuvedena - pak se nic provádět nebude.
     * */
    public DefaultDict(Map<K,V> base,
                       com.markussecundus.forms.utils.function.Function<K,V> supplier,
                       com.markussecundus.forms.utils.function.Function<K,K> keyTransform,
                       com.markussecundus.forms.utils.function.BiConsumer<K,V> afterValueCreated){
        this.base = base;
        this.supplier=supplier;
        this.keyTransform = keyTransform;
        this.afterValueCreated = afterValueCreated;
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
    public DefaultDict(com.markussecundus.forms.utils.function.Function<K,V> supplier,
                       com.markussecundus.forms.utils.function.Function<K,K> keyTransform){
        this(DEF_MAP_INSTANCE_FACTORY(), supplier, keyTransform);
    }

    /**
     * Vytvoří instanci wrapperu nad daným objektem {@link Map} a používající daný generátor
     * výchozích hodnot.
     *
     * @param base instance {@link Map}, nad kterou bude tento objekt fungovat jako wrapperu
     * @param supplier generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
     * @param keyTransform Funkce, která podle tázaného klíče vygeneruje skutečný klíč, pod kterým bude uložena vegenerovaná hodnota.
     *                     Nová hodnota musí být ekvivalentní s hodnotou původní podle funkce <code>Objects.equals</code>.
     *                     Identita pokud není udána, nebo je <code>null</code>.
     * */
    public DefaultDict(Map<K,V> base,
                       com.markussecundus.forms.utils.function.Function<K,V> supplier,
                       com.markussecundus.forms.utils.function.Function<K,K> keyTransform){
        this(base, supplier, keyTransform, null);
    }

    /**
     * Vytvoří instanci wrapperu nad daným objektem {@link Map} a používající daný generátor
     * výchozích hodnot.
     *
     * @param base instance {@link Map}, nad kterou bude tento objekt fungovat jako wrapperu
     * @param supplier generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
     * */
    public DefaultDict(Map<K,V> base, com.markussecundus.forms.utils.function.Function<K,V> supplier){
        this(base, supplier, null, null);
    }

    /**
     * Vytvoří instanci wrapperu nad novou, prázdnou instancí {@link Map} a používající daný generátor
     * výchozích hodnot.
     *
     * @param supplier generátor výchozích hodnot; jako parametr přebírá klíč, pro který je hodnota generována
     * */
    public DefaultDict(com.markussecundus.forms.utils.function.Function<K,V> supplier){this(DEF_MAP_INSTANCE_FACTORY(), supplier);}

    private static<K,V> Map<K,V> DEF_MAP_INSTANCE_FACTORY(){return new HashMap<>();}

    /**
     * Báze, na kterou wrapper odkazuje.
     * */
    public final Map<K,V> base;

    /**
     * Generátor výchozích hodnot.
     * */
    private final com.markussecundus.forms.utils.function.Function<K,V> supplier;

    private final com.markussecundus.forms.utils.function.Function<K,K> keyTransform;
    private final com.markussecundus.forms.utils.function.BiConsumer<K, V> afterValueCreated;

    /**
     * {@inheritDoc}
     * */
    @Override
    public int size() {
        return base.size();
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean containsKey(Object key) {
        return base.containsKey(key);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean containsValue(Object value) {
        return base.containsValue(value);
    }

    /**
     * Vrátí hodnotu v {@link Map}ě se nacházející, příp. nově vygenerovanou.
     *
     * {@inheritDoc}
     *
     * @return <code>null</code> pokud nebyla nalezena nenullová hodnota a novou se nepodařilo vygenerovat,
     * příp. generátor vygeneroval <code>null</code>. Jinak vrátí stávající nalezenou, či nově vygenerovanou hodnotu.
     * */
    @Override
    public V get(Object key) {
        V ret =  base.get(key);
        if(ret==null){
            try {
                K key_k = (K) key;
                ret = supplier.apply(key_k);

                if(keyTransform!=null) {
                    K trans = keyTransform.apply(key_k);
                    if(!FormsUtil.equals(trans, key_k))
                        throw new RuntimeException("DefaultDict: Transform function for keys must return a value, that is equal with the original key value");
                    key_k = trans;
                }

                base.put(key_k, ret);

                if(afterValueCreated!=null)
                    afterValueCreated.accept(key_k, ret);
            }catch(Exception e){
                return null;
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public V put(K key, V value) {
        return base.put(key, value);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public V remove(Object key) {
        return base.remove(key);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        base.putAll(m);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void clear() {
        base.clear();
    }

    /**{@inheritDoc}*/@Override
    public Set<K> keySet() {
        return base.keySet();
    }

    /**{@inheritDoc}*/@Override
    public Collection<V> values() {
        return base.values();
    }

    /**{@inheritDoc}*/@Override
    public Set<Entry<K, V>> entrySet() {
        return base.entrySet();
    }

    /**{@inheritDoc}*/@Override
    public V getOrDefault(Object key, V defaultValue) {
        return base.getOrDefault(key, defaultValue);
    }

    /**{@inheritDoc}*/@Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        base.forEach(action);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        base.replaceAll(function);
    }

    /**{@inheritDoc}*/@Override
    public V putIfAbsent(K key, V value) {
        return base.putIfAbsent(key, value);
    }

    /**{@inheritDoc}*/@Override
    public boolean remove(Object key, Object value) {
        return base.remove(key, value);
    }

    /**{@inheritDoc}*/@Override
    public boolean replace(K key, V oldValue, V newValue) {
        return base.replace(key, oldValue, newValue);
    }

    /**{@inheritDoc}*/@Override
    public V replace(K key, V value) {
        return base.replace(key, value);
    }

    /**{@inheritDoc}*/@Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return base.computeIfAbsent(key, mappingFunction);
    }

    /**{@inheritDoc}*/@Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return base.computeIfPresent(key, remappingFunction);
    }

    /**{@inheritDoc}*/@Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return base.compute(key, remappingFunction);
    }

    /**{@inheritDoc}*/@Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return base.merge(key, value, remappingFunction);
    }
}
