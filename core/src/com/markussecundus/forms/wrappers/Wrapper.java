package com.markussecundus.forms.wrappers;


import com.markussecundus.forms.utils.function.Supplier;
import com.markussecundus.forms.wrappers.property.ConstProperty;


/**
 * Základní rozhraní pro Wrapperový typ, který umí hodnotu, na kterou odkazuje, číst i upravovat.
 *
 * @param <T> typ na který wrapper ukazuje
 *
 * @see WriteonlyWrapper
 * @see ReadonlyWrapper
 *
 * @see com.markussecundus.forms.wrappers.property.Property
 *
 * @see ConstProperty
 * @see com.markussecundus.forms.wrappers.property.ReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.WriteonlyProperty
 *
 * @author MarkusSecundus
 * */
public interface Wrapper<T> extends ReadonlyWrapper<T>, WriteonlyWrapper<T> {

    @Override
    public T get();

    @Override
    public T set(T t);


    /**
     * Kanonická parciální implementace {@link Wrapper}u,
     * která poskytuje funkce <code>equals</code>, <code>hashCode</code>
     * a <code>toString</code> odkazující na stejné funkce vnitřního objektu.
     *
     * @param <T> typ, na který wrapper ukazuje
     *
     * @author MarkusSecundus
     * */
    public abstract static class AbstractSimpleWrapper<T> extends ReadonlyWrapper.AbstractSimpleWrapper<T> implements Wrapper<T>{}


    /**
     * @return Wrapper získaný složením dohromady obou wrapperů s poloviční funkcionalitou
     * */
    public static<T> Wrapper<T> make(ReadonlyWrapper<T> readPrototype, WriteonlyWrapper<T> writePrototype){
        return new AbstractSimpleWrapper<T>() {
            /**
             * Odkazuje na metodu ve <code>writePrototype</code>.
             *
             * {@inheritDoc}
             * */
            @Override public T set(T t) {
                return writePrototype.set(t);
            }
            /**
             * Odkazuje na metodu v <code>readPrototype</code>.
             *
             * {@inheritDoc}
             * */
            @Override public T get() {
                return readPrototype.get();
            }


            /**
             * Odkazuje na metodu v <code>readPrototype</code>.
             *
             * {@inheritDoc}
             * */
            @Override
            public boolean equals(Object obj) {
                return readPrototype.equals(obj);
            }
            /**
             * Odkazuje na metodu v <code>readPrototype</code>.
             *
             * {@inheritDoc}
             * */
            @Override
            public int hashCode() {
                return readPrototype.hashCode();
            }
            /**
             * Odkazuje na metodu v <code>readPrototype</code>.
             *
             * {@inheritDoc}
             * */
            @Override
            public String toString() {
                return readPrototype.toString();
            }
        };
    }

    /**
     * @param t iniciální hodnota pro vytvářený {@link Wrapper}
     * @param <T> typ na který wrapper ukazuje
     *
     * @return {@link Wrapper} v kanonické implementaci inicializovaný danou hodnotou
     * */
    public static<T> Wrapper<T> make(T t){
        return new SimpleWrapper<>(t);
    }

    /**
     * @param sup generátor iniciální hodnoty pro líný {@link Wrapper}
     * @param <T> typ na který wrapper ukazuje
     *
     * @return Líný {@link Wrapper} v kanonické implementaci inicializovaný daným generátorem
     * */
    public static<T> Wrapper<T> makeLazy(Supplier<T> sup){return new LazyWrapper<>(sup);}








    /**
     * Kanonická implementace {@link Wrapper}u přechovávající odkazovaný objekt přímo v sobě.
     *
     * @param <T> typ, na který wrapper ukazuje
     *
     * @author MarkusSecundus
     * */
    public static class SimpleWrapper<T> extends AbstractSimpleWrapper<T>{
    //public:
        /**
         * @param init inciciální hodnota
         *
         * Vytvoří Wrapper a inicializuje ho danou hodnotou
         * */
        public SimpleWrapper(T init){ set(init); }

        /**{@inheritDoc}*/
        @Override public T get() {
            return item;
        }

        /**{@inheritDoc}*/
        @Override public T set(T t) {
            return this.item = t;
        }

     //private:
        private T item;
    }

    /**
     * Kanonická implementace {@link Wrapper}u přechovávající odkazovaný objekt přímo v sobě
     * a generující ho líně až když je poprvé vyžádán, pokud dříve již nebyla nastavena nějaká hodnota.
     *
     * @param <T> typ, na který wrapper ukazuje
     *
     * @author MarkusSecundus
     * */
    public static class LazyWrapper<T> extends AbstractSimpleWrapper<T>{
    //public:
        /**
         * Vytvoří Wrapper s danou generující funkcí.
         *
         * @param generator vygeneruje hodnotu pro odkazovaný objekt, až bude potřeba
         * */
        public LazyWrapper(Supplier<T> generator){this.sup= generator;}

        @Override
        public T set(T o) {
            sup = null;
            return t;
        }

        @Override
        public T get() {
            useSup();
            return t;
        }

    //private:
        private T t;
        private Supplier<T> sup;

        private void useSup(){
            if(sup==null)return;
            t = sup.get();
            sup = null;
        }

    }
}
