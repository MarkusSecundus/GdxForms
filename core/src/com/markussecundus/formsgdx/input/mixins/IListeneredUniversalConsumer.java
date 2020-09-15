package com.markussecundus.formsgdx.input.mixins;

import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.interfaces.ListeneredUniversalConsumer;


/**
 * Mixin-Rozhraní Implementující skrze defaultní metody veškerou funkcionalitu {@link ListeneredUniversalConsumer}.
 * <p></p>
 * Účel je, aby by bylo jednoduše možné přidat požadovanou funkcionalitu i do tříd,
 * které již mají předka a nemohou dědit z další třídy, aby získaly funkcionalitu zpracování vstupu.
 *<p></p>
 * Každé své instanci poskytuje Delegáty náležící daným vstupním událostem.
 *<p></p>
 *
 * Definuje velmi ošklivě pojmenované pomocné a konfigurační metody, které nikomu z venčí nikdy k ničemu
 * nebudou a ideálně by měly být <code>protected</code>, kdyby to Java dovolovala.
 * <p>
 * Pro jejich skrytí před náhodným uživatelem vašich implementací lze použít např. tento pattern:
 * <pre>
 * <code>
 *
 *     //abstraktní třída implementuje pouze čisté rozhraní bez implementace
 *      abstract class ExampleLayout extends BasicLinearLayout<BasicRenderer, Vect2f, Float> implements ListeneredUniversalConsumer{
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
 *         private static class ListenersAdded extends ExampleLayout implements IListeneredUniversalConsumer.ForLayout{
 *             private ListenersAdded(Vect2f prefSize, VectUtil<Vect2f, Float> posUtil) { super(prefSize, posUtil); }
 *         }
 *     }
 * </code>
 *</pre>
 *
 * @see InputConsumer
 *
 * @see ListeneredUniversalConsumer
 *
 * @see IListeneredTouchConsumer
 * @see com.markussecundus.formsgdx.input.mixins.IListeneredScrollConsumer
 * @see IListeneredKeyConsumer
 *
 * @author MarkusSecundus
 * */
public interface IListeneredUniversalConsumer extends IListeneredKeyConsumer, IListeneredScrollConsumer, IListeneredTouchConsumer , ListeneredUniversalConsumer {

    public static interface ForLayout extends IListeneredUniversalConsumer, IListeneredTouchConsumer.ForLayout{}

}
