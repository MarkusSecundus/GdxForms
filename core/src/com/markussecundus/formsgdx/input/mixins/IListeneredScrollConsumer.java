package com.markussecundus.formsgdx.input.mixins;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.extensibility.Extensible;
import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.args.OnScrolledArgs;
import com.markussecundus.formsgdx.input.interfaces.ListeneredScrollConsumer;

/**
 * Mixin-Rozhraní Implementující skrze defaultní metody veškerou funkcionalitu {@link ListeneredScrollConsumer}.
 *<p>
 * Účel je, aby by bylo jednoduše možné přidat požadovanou funkcionalitu i do tříd,
 * které již mají předka a nemohou dědit z další třídy, aby získaly funkcionalitu zpracování vstupu.
 *</p>
 * <p>
 * Každé své instanci poskytuje Delegáty náležící daným vstupním událostem. Ty budou vytvořeny všechny
 * najednou při prvním vyžádání nějakého z nich.
 *</p>
 *<p>
 * Definuje velmi ošklivě pojmenované pomocné a konfigurační metody, které nikomu z venčí nikdy k ničemu
 * nebudou a ideálně by měly být <code>protected</code>, kdyby to Java dovolovala.
 * Pro jejich skrytí před náhodným uživatelem vašich implementací lze použít např. tento pattern:
 * </p>
 * <pre>
 * <code>
 *
 *     //abstraktní třída implementuje pouze čisté rozhraní bez implementace
 *      abstract class ExampleLayout extends BasicLinearLayout<BasicRenderer, Vect2f, Float> implements ListeneredScrollConsumer{
 *
 *         protected ExampleLayout(Vect2f prefSize, VectUtil<Vect2f, Float> posUtil) { super(prefSize, posUtil); }
 *
 *
 *         //factory, přes kterou jedině lze instanci třídy získat, vrací novou instanci privátní podtřídy
 *
 *         public static ExampleLayout make(Vect2f prefSize, VectUtil<Vect2f, Float> posUtil){
 *             return new ListenersAdded(prefSize, posUtil);
 *         }
 *
 *         //... veškerá implementace třídy
 *         //...
 *         //...
 *
 *
 *          //privátní neabstraktní potomek třídy implementuje Implementační variantu ListeneredConsumer rozhraní
 *
 *         private static class ListenersAdded extends ExampleLayout implements ILListeneredScrollConsumer{
 *             private ListenersAdded(Vect2f prefSize, VectUtil<Vect2f, Float> posUtil) { super(prefSize, posUtil); }
 *         }
 *     }
 * </code>
 *</pre>
 *
 * @see InputConsumer
 *
 * @see ListeneredScrollConsumer
 *
 * @see IListeneredTouchConsumer
 * @see IListeneredKeyConsumer
 * @see IListeneredUniversalConsumer
 *
 * @author MarkusSecundus
 * */
public interface IListeneredScrollConsumer extends InputConsumer, ListeneredScrollConsumer, Extensible {


    /**{@inheritDoc}*/
    @Override
    default ConstProperty<EventDelegate<OnScrolledArgs>> onScrollListener(){
        return Util.getImpl(this).onScrolled;
    }




    /**
     * Factory na instance třídy obsahující vnitřní logiku mixinové komponenty.
     * K přepsání pro Mixin-Rozhraní potomky přidávající funkcionalitu děděním z vnitřní třídy mixinové komponenty.
     *
     * @return Nová instance vnitřní třídy mixinové komponenty
     * */
    default Util.Impl __ListeneredScrollConsumer__MakeInstance(){
        return new Util.Impl(this);
    }

    /**
     * Factory na {@link EventDelegate}, které budou sloužit jako handlery vstupních událostí.
     * Přepište, pokud chcete v potomkovi použít jinou, než kanonickou implementaci {@link EventDelegate}.
     *
     * @return Nová instance {@link EventDelegate}
     * */
    default <T> EventDelegate<T> __ListeneredScrollConsumer_option__MakeEventDelegate(){return EventDelegate.make();}

    /**
     * Statická třída obsahující privátní atributy tohoto Mixin-Rozhraní (Interface v Javě z nějakého důvodu nesmí mít privátní atributy).
     *
     * @author MarkusSecundus
     * */
    static final class Util {
        private static final Function<Extensible, Impl> INSTANCE_SUPPLIER = self->((IListeneredScrollConsumer)self).__ListeneredScrollConsumer__MakeInstance();

        /**
         * (pozn.: V žádném případě nesmí být volána v rámci konfiguračních metod již tázané mixinové komponenty.)
         *
         * @return Mixinová komponenta příslušící dané instanci {@link IListeneredScrollConsumer}
         * */
        protected static Impl getImpl(IListeneredScrollConsumer self){
            return self.getExtension(Util.Impl.class, INSTANCE_SUPPLIER);
        }

        /**
         * Implementace a datový kontejner vnitřní mixinové komponenty, na kterou je rozhraní přesměrováváno.
         *
         * @author MarkusSecundus
         * */
        protected static class Impl{
            /**
             * Vytvoří instanci mixinové komponenty pro danou instanci {@link IListeneredScrollConsumer}.
             * */
            public Impl(IListeneredScrollConsumer self){
                onScrolled = new SimpleConstProperty<>(self.__ListeneredScrollConsumer_option__MakeEventDelegate());
            }
            /**
             *  Delegát zpracovávající volání <code>scrolled</code>.
             * */
            public final ConstProperty<EventDelegate<OnScrolledArgs>> onScrolled;
        }
    }
}
