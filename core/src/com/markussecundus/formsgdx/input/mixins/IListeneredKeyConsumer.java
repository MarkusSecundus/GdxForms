package com.markussecundus.formsgdx.input.mixins;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.datastruct.DefaultDict;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.args.OnKeyClickedArgs;
import com.markussecundus.formsgdx.input.args.OnKeyTypedArgs;
import com.markussecundus.formsgdx.input.interfaces.ListeneredKeyConsumer;

import java.util.Map;

/**
 * Mixin-Rozhraní Implementující skrze defaultní metody veškerou funkcionalitu {@link ListeneredKeyConsumer}.
 *
 * Účel je, aby by bylo jednoduše možné přidat požadovanou funkcionalitu i do tříd,
 * které již mají předka a nemohou dědit z další třídy, aby získaly funkcionalitu zpracování vstupu.
 *
 * Každé své instanci poskytuje Delegáty náležící daným klávesnicovým událostem. Ty budou vytvořeny všechny
 * najednou při prvním vyžádání nějakého z nich.
 *
 *
 * Definuje velmi ošklivě pojmenované pomocné a konfigurační metody, které nikomu z venčí nikdy k ničemu
 * nebudou a ideálně by měly být <code>protected</code>, kdyby to Java dovolovala.
 * Pro jejich skrytí před náhodným uživatelem vašich implementací lze použít např. tento pattern:
 * <pre>
 * <code>
 *
 *     //abstraktní třída implementuje pouze čisté rozhraní bez implementace
 *      abstract class ExampleLayout extends BasicLinearLayout<BasicRenderer, Vect2f, Float> implements ListeneredKeyConsumer{
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
 *         private static class ListenersAdded extends ExampleLayout implements IListeneredKeyConsumer{
 *             private ListenersAdded(Vect2f prefSize, VectUtil<Vect2f, Float> posUtil) { super(prefSize, posUtil); }
 *         }
 *     }
 * </code>
 *</pre>
 *
 * @see com.markussecundus.formsgdx.input.InputConsumer
 *
 * @see ListeneredKeyConsumer
 *
 * @see IListeneredTouchConsumer
 * @see IListeneredScrollConsumer
 * @see IListeneredUniversalConsumer
 *
 * @author MarkusSecundus
 * */
public interface IListeneredKeyConsumer extends InputConsumer, ListeneredKeyConsumer {


    /**{@inheritDoc}*/
    @Override
    default ConstProperty<EventDelegate<OnKeyClickedArgs>> onKeyDownListener(){
        return Util.getImpl(this).onKeyDown;
    }
    /**{@inheritDoc}*/
    @Override
    default ConstProperty<EventDelegate<OnKeyClickedArgs>> onKeyUpListener(){
        return Util.getImpl(this).onKeyUp;
    }
    /**{@inheritDoc}*/
    @Override
    default ConstProperty<EventDelegate<OnKeyTypedArgs>> onKeyTypedListener(){
        return Util.getImpl(this).onKeyTyped;
    }



    /**
     * Factory na instance třídy obsahující vnitřní logiku mixinové komponenty.
     * K přepsání pro Mixin-Rozhraní potomky přidávající funkcionalitu děděním z vnitřní třídy mixinové komponenty.
     *
     * @return Nová instance vnitřní třídy mixinové komponenty
     * */
    default Util.Impl __ListeneredKeyConsumer__MakeInstance(){
        return new Util.Impl(this);
    }


    /**
     * Factory na {@link EventDelegate}, které budou sloužit jako handlery vstupních událostí.
     * Přepište, pokud chcete v potomkovi použít jinou, než kanonickou implementaci {@link EventDelegate}.
     *
     * @return Nová instance {@link EventDelegate}
     * */
    default <T> EventDelegate<T> __ListeneredKeyConsumer_option__MakeEventDelegate(){return EventDelegate.make();}

    /**
     * Statická třída obsahující privátní atributy tohoto Mixin-Rozhraní (Interface v Javě z nějakého důvodu nesmí mít privátní atributy).
     *
     * @author MarkusSecundus
     * */
    static final class Util {
        private static final Map<FormsUtil.WrapperForReferenceComparison<IListeneredKeyConsumer>, Impl> impls = new DefaultDict<>(
                self -> self.item.__ListeneredKeyConsumer__MakeInstance(),
                FormsUtil.WrapperForReferenceComparison::cpy);

        private static final FormsUtil.WrapperForReferenceComparison<IListeneredKeyConsumer> instanceFinder = new FormsUtil.WrapperForReferenceComparison<>(null);

        /**
         * (pozn.: V žádném případě nesmí být volána v rámci konfiguračních metod již tázané mixinové komponenty.)
         *
         * @return Mixinová komponenta příslušící dané instanci {@link IListeneredKeyConsumer}
         * */
        protected static Impl getImpl(IListeneredKeyConsumer self){
            return impls.get(instanceFinder.with(self));
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
            public Impl(IListeneredKeyConsumer self){
                onKeyDown = new SimpleConstProperty<>(self.__ListeneredKeyConsumer_option__MakeEventDelegate());
                onKeyUp = new SimpleConstProperty<>(self.__ListeneredKeyConsumer_option__MakeEventDelegate());
                onKeyTyped = new SimpleConstProperty<>(self.__ListeneredKeyConsumer_option__MakeEventDelegate());
            }
            /**
             *  Delegát zpracovávající volání <code>keyDown</code>.
             * */
            protected final ConstProperty<EventDelegate<OnKeyClickedArgs>> onKeyDown;
            /**
             *  Delegát zpracovávající volání <code>keyUp</code>.
             * */
            protected final ConstProperty<EventDelegate<OnKeyClickedArgs>> onKeyUp;
            /**
             *  Delegát zpracovávající volání <code>keyTyped</code>.
             * */
            protected final ConstProperty<EventDelegate<OnKeyTypedArgs>> onKeyTyped;
        }


    }
}
