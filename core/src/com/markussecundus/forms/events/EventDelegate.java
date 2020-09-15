package com.markussecundus.forms.events;

import com.markussecundus.forms.utils.function.BiPredicate;
import com.markussecundus.forms.wrappers.property.Property;

import java.util.List;


/**
 * {@link EventListener}, který sám o sobě nic nedělá, ale slouží jako kontejner
 * pro další {@link EventListener}y.
 *
 * @see IEventDelegate
 * @see EventListener
 * @see EventListenerX
 *
 * @author MarkusSecundus
 * */
public interface EventDelegate<Args> extends EventListener<Args> {


    /**
     * Vrací novou instanci kanonické implementace tohoto rozhraní.
     *
     * @return Nová instance kanonické implementace {@link EventDelegate}
     * */
    public static <T> EventDelegate<T> make(){return new IEventDelegate<>();}



    /**
     * @return List listenerů náležících k dané prioritě
     * */
    public List<EventListener<? super Args>> getListeners(Integer priority);


    /**
     * {@link List}, do kterého je možné přidávat Listenery, příp. s nimi
     * jakkoliv jinak manipulovat.
     *
     * Listenery se budou provádět v pořadí, v jakém se v Listu nalézají,
     * od 0. po poslední,
     * dokud nějaký z nich nevrátí <code>false</code> - tím se vykonávání
     * Delegáta ukončí a žádný další listener vykonán nebude.
     *
     * @return list {@link EventListener}ů tohoto Delegáta
     * */
    public default List<EventListener<? super Args>> getUserListeners(){ return getListeners(ListenerPriorities.USER); }

    /**
     * Obsahuje listenery, které se provedou na začátku volání Delegáta,
     * před Listenery v <code>getListeners</code>.
     *
     *
     * Listenery se budou provádět v pořadí, v jakém se v Listu nalézají,
     * od 0. po poslední,
     * dokud nějaký z nich nevrátí <code>false</code> - tím se vykonávání
     * Delegáta ukončí a žádný další listener vykonán nebude.
     *
     * Sem přidávají své listenery utility formulářových knihoven.
     * Náhodný uživatel by s tímto neměl manipulovat,
     * pokud si není naprosto jistý, co dělá.
     *
     * @return list {@link EventListener}ů tohoto Delegáta
     * */
    public default List<EventListener<? super Args>> _getUtilListeners(){
        return getListeners(ListenerPriorities.PRE_UTIL);
    }

    /**
     * Obsahuje listenery, které se provedou na konci volání Delegáta,
     * až po Listenerech v <code>getListeners</code>.
     *
     *
     * Listenery se budou provádět v pořadí, v jakém se v Listu nalézají,
     * od 0. po poslední,
     * dokud nějaký z nich nevrátí <code>false</code> - tím se vykonávání
     * Delegáta ukončí a žádný další listener vykonán nebude.
     *
     * Sem přidávají své listenery utility formulářových knihoven.
     * Náhodný uživatel by s tímto neměl manipulovat,
     * pokud si není naprosto jistý, co dělá.
     *
     * @return list {@link EventListener}ů tohoto Delegáta
     * */
    public default List<EventListener<? super Args>> _getPostUtilListeners(){
        return getListeners(ListenerPriorities.POST_UTIL);
    }

    /**
     * Pokud je kdekoliv v delegátovi přítomen, odebere daný listener, nezávisle na jeho prioritě.
     * */
    public boolean removeListener(EventListener<?> list);

    /**
     * Odebere neprosto všechny listenery náležící danému delegátovi nezávisle na jejich prioritě -
     * potenciálně i systémové listenery, jež by smazány být neměly.
     * */
    public void clear();



    /**
     *
     * Odkaz na {@link ListenerPriorities} viditelný přímo jako prvek {@link EventDelegate} rozhraní.
     * <p>
     * Přidán pro větší přehlednost.
     *
     * @see ListenerPriorities
     * */
    public final class Priorities extends ListenerPriorities{}



    /**
     *
     * Určuje, jakou návratovou hodnotu bude mít Delegát v závislosti na návratové hodnotě jeho prvků.
     *
     * @return Vlastnost, jakou návratovou hodnotu bude mít Delegát v závislosti na návratové hodnotě jeho prvků.
     * */
    public Property<ReturnValuePolicy> returnValuePolicy();

    /**
     *
     * Pohodlnější zkratka pro <code>returnValuePolicy().get()</code>.
     *
     * @return Pohodlnější zkratka pro <code>returnValuePolicy().get()</code>.
     * */
    public default ReturnValuePolicy getReturnValuePolicy(){return returnValuePolicy().get();}

    /**
     *
     * Pohodlnější zkratka pro <code>returnValuePolicy().set(pol)</code>.
     *
     * @param pol nová hodnota pro <code>returnValuePolicy</code>
     * */
    public default ReturnValuePolicy setReturnValuePolicy(ReturnValuePolicy pol){ return returnValuePolicy().set(pol); }


    /**
     *
     * Určuje, jakou návratovou hodnotu bude mít Delegát
     * v závislosti na návratové hodnotě jeho prvků.
     *
     * Pokud je vykonávání Delegáta ukončeno předčasně, je mu
     * předáno <code>false</code>, v opačném případě <code>true</code>.
     *
     * @author MarkusSecundus
     * */
    @FunctionalInterface
    public static interface ReturnValuePolicy{
        boolean convRetVal(boolean retVal);

        /**
         *
         * Delegát vždy vrací <code>true</code>, bez ohledu na to, jestli jeho vykonávání
         * bylo ukončeno předčasně či ne.
         * */
        public static final ReturnValuePolicy ALWAYS_TRUE = new ReturnValuePolicy() {
            @Override
            public boolean convRetVal(boolean retVal) {
                return true;
            }

            @Override
            public String toString() {
                return "ALWAYS_TRUE";
            }
        };

        /**
         *
         * Delegát vždy vrací <code>false</code>, bez ohledu na to, jestli jeho vykonávání
         * bylo ukončeno předčasně či ne.
         * */
        public static final ReturnValuePolicy ALWAYS_FALSE = new ReturnValuePolicy() {
            @Override
            public boolean convRetVal(boolean retVal) {
                return false;
            }

            @Override
            public String toString() {
                return "ALWAYS_FALSE";
            }
        };

        /**
         *
         * Delegát vrací <code>false</code> pokud jeho vykonávání bylo ukončeno předčasně
         * (tj. nějaký jeho prvek vrátil <code>false</code>), v opačném případě
         * vrací <code>true</code>.
         * */
        public static final ReturnValuePolicy USE_CHILD = new ReturnValuePolicy() {
            @Override
            public boolean convRetVal(boolean retVal) {
                return retVal;
            }

            @Override
            public String toString() {
                return "USE_CHILD";
            }
        };

        /**
         *
         * Delegát vrací <code>true</code> pokud jeho vykonávání bylo ukončeno předčasně
         * (tj. nějaký jeho prvek vrátil <code>false</code>), v opačném případě
         * vrací <code>false</code>.
         * */
        public static final ReturnValuePolicy INVERSE_CHILD= new ReturnValuePolicy() {
            @Override
            public boolean convRetVal(boolean retVal) {
                return !retVal;
            }

            @Override
            public String toString() {
                return "INVERSE_CHILD";
            }
        };
    }


    /**
     *
     * Výchozí hodnota pro {@link ReturnValuePolicy} v každé implementaci {@link EventDelegate},
     * pokud není uvedeno jinak.
     * */
    public static final ReturnValuePolicy DEFAULT_RET_VAL_POLICY = ReturnValuePolicy.USE_CHILD;


    /**
     *
     * Provede postupně všechny Listenery, v pořadí, v jakém jsou přítomny
     * postupně v <code>_getUtilListeners</code>, <code>getListeners</code> a <code>_getPostUtilListners</code>.
     * Pokud nějaký listener vrátí false, vykonávání Delegáta bude přerušeno.
     * Návratová hodnota bude určena pomocí <code>returnValuePolicy</code> ( -viz {@link ReturnValuePolicy})
     *
     * @param e argumenty, které budou předány vnitřním listenerům
     *
     * @return znamení zda má smysl pokračovat ve vykonávání náseldující činnosti; závisí na stavu <code>returnValuePolicy</code>
     * */
    @Override public boolean exec(Args e);

    /**
     * Přidá daný {@link EventListenerX} do seznamu listenerů v
     * <code>getListeners</code>.
     *
     * Slouží pro větší pohodlí uživatelů při psaní lambd
     * (aby nemuseli všude explicitně vracet <code>true</code>).
     *
     * Pouze ve verzi pro <code>getListeners</code>,
     * aby uživatele demotivovala šahat do <code>_utilListener</code>u.
     *
     * @param list listener pro přidání do Delegáta
     * */
    public default void add(EventListenerX<? super Args> list){
        getListeners(Priorities.USER).add(list);
    }


    /**
     * Právě vykonávaný listener má být vymazán, načež se má pokračovat
     * ve vykonávání zbytku listenerů daného Delegáta, jako kdyby listener vrátil <code>true</code>.
     *
     * @see ApplyOnParentDelegate
     * */
    public static final ApplyOnParentDelegate DELETE_SELF = ApplyOnParentDelegate.make((d,l)->{d.removeListener(l);return true;});


    /**
     * Právě vykonávaný listener má být vymazán,
     * a vykonávání Delegáta má být přerušeno, jako by
     * listener vrátil <code>false</code>.
     *
     * @see ApplyOnParentDelegate
     * */
    public static final ApplyOnParentDelegate DELETE_SELF_AND_ABORT = ApplyOnParentDelegate.make((d,l)->{d.removeListener(l);return false;});



    /**
     * Když je vyhozena z listeneru, delegát ji odchytí a provede na sobě její metodu <code>exec</code>.
     * */
    public static abstract class ApplyOnParentDelegate extends RuntimeException{
        private ApplyOnParentDelegate(){}

        /**
         * Akce, kterou má na sobě delegát provést.
         *
         * @param parent delegát, jež zavolal listener vyhodivší tuto instanci {@link ApplyOnParentDelegate}
         * @param currentlyExecuted listener vyhodivší tuto instanci {@link ApplyOnParentDelegate}
         *
         * @return návratová hodnota, jaká má být přiznána listeneru, jež tuto výjimku vyhodil
         * (<code>false</code> pokud má být běh delegáta přerušen).
         * */
        public abstract boolean exec(EventDelegate<?> parent, EventListener<?> currentlyExecuted);

        /**
         * @return instance {@link ApplyOnParentDelegate}, provádějící danou akci na otcovském delegátu, berouc
         * v úvahu listener, ze kterého byla výjimka hozena.
         * */
        public static ApplyOnParentDelegate make(BiPredicate<EventDelegate<?>, EventListener<?>> action){
            return new ApplyOnParentDelegate() {
                public boolean exec(EventDelegate<?> parent, EventListener<?> currentlyExecuted) {
                    return action.test(parent, currentlyExecuted);
                }
            };
        }
        /**
         * @return instance {@link ApplyOnParentDelegate}, provádějící danou akci na otcovském delegátu.
         * */
        public static ApplyOnParentDelegate make(EventListener<EventDelegate<?>> action){
            return new ApplyOnParentDelegate() {
                public boolean exec(EventDelegate<?> parent, EventListener<?> currentlyExecuted) {
                    return action.exec(parent);
                }
            };
        }
    }
}
