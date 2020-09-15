package com.markussecundus.formsgdx.input.mixins;

import com.markussecundus.forms.elements.DrawableElem;
import com.markussecundus.forms.elements.UberDrawable;
import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.events.ListenerPriorities;
import com.markussecundus.forms.extensibility.Extensible;
import com.markussecundus.forms.gfx.Drawable;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.utils.function.BiFunction;
import com.markussecundus.forms.utils.function.BiPredicate;
import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.utils.function.Supplier;
import com.markussecundus.forms.utils.vector.Vect2d;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.Vect2i;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.InputManager;
import com.markussecundus.formsgdx.input.args.OnMouseMovedArgs;
import com.markussecundus.formsgdx.input.args.OnTouchArgs;
import com.markussecundus.formsgdx.input.args.OnTouchDraggedArgs;
import com.markussecundus.formsgdx.input.interfaces.ListeneredTouchConsumer;

import java.util.HashMap;
import java.util.Map;

/**
 * Mixin-Rozhraní Implementující skrze defaultní metody veškerou funkcionalitu {@link ListeneredTouchConsumer}.
 * <p></p>
 * Účel je, aby by bylo jednoduše možné přidat požadovanou funkcionalitu i do tříd,
 * které již mají předka a nemohou dědit z další třídy, aby získaly funkcionalitu zpracování vstupu.
 * <p></p>
 * Každé své instanci poskytuje Delegáty náležící daným vstupním událostem. Ty budou vytvořeny všechny
 * najednou při prvním vyžádání nějakého z nich. Taktéž těmto Delegátům rovnou vygeneruje
 * prvky poskytující základní, obecně užitečnou logiku pro zpracování vstupu z obrazovky
 * blíže konfigurovatelnou / vypnutelnou přepsáním pomocných metod
 * (označených <code>__ListeneredTouchConsumer_...</code>)
 * <p></p>
 *
 * Definuje velmi ošklivě pojmenované pomocné a konfigurační metody, které nikomu z venčí nikdy k ničemu
 * nebudou a ideálně by měly být <code>protected</code>, kdyby to Java dovolovala.
 * <p>
 * Pro jejich skrytí před náhodným uživatelem vašich implementací lze použít např. tento pattern:
 * <pre>
 * <code>
 *
 *     //abstraktní třída implementuje pouze čisté rozhraní bez implementace
 *      abstract class ExampleLayout extends BasicLinearLayout<BasicRenderer, Vect2f, Float> implements ListeneredTouchConsumer{
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
 *         private static class ListenersAdded extends ExampleLayout implements IListeneredTouchConsumer.ForLayout{
 *             private ListenersAdded(Vect2f prefSize, VectUtil<Vect2f, Float> posUtil) { super(prefSize, posUtil); }
 *         }
 *     }
 * </code>
 *</pre>
 *
 * @see com.markussecundus.formsgdx.input.InputConsumer
 *
 * @see ListeneredTouchConsumer
 *
 * @see com.markussecundus.formsgdx.input.mixins.IListeneredScrollConsumer
 * @see IListeneredKeyConsumer
 * @see IListeneredUniversalConsumer
 *
 * @author MarkusSecundus
 * */
public interface IListeneredTouchConsumer extends InputConsumer, ListeneredTouchConsumer, Extensible {

    @Override
    default boolean isClicked(){return Util.getImpl(this).isClicked;}


    @Override
    default ConstProperty<EventDelegate<OnTouchArgs>> onTouchDownListener(){
        return Util.getImpl(this).onTouchDown;
    }
    @Override
    default ConstProperty<EventDelegate<OnTouchArgs>> onTouchUpListener(){
        return Util.getImpl(this).onTouchUp;
    }
    @Override
    default ConstProperty<EventDelegate<OnTouchDraggedArgs>> onTouchDraggedListener(){
        return Util.getImpl(this).onTouchDragged;
    }
    @Override
    default ConstProperty<EventDelegate<OnMouseMovedArgs>> onMouseMovedListener(){
        return Util.getImpl(this).onMouseMoved;
    }


    @Override
    default ConstProperty<EventDelegate<OnTouchArgs>> onClickedListener(){
        return Util.getImpl(this).onClicked;
    }
    @Override
    default ConstProperty<EventDelegate<OnTouchArgs>> onUnclickedListener(){
        return Util.getImpl(this).onUnclicked;
    }

    /**
     * Factory na instance třídy obsahující vnitřní logiku mixinové komponenty.
     * K přepsání pro Mixin-Rozhraní potomky přidávající funkcionalitu děděním z vnitřní třídy mixinové komponenty.
     *
     * @return Nová instance vnitřní třídy mixinové komponenty
     * */
    default Util.Impl __ListeneredTouchConsumer__MakeInstance(){
        return new Util.Impl(this);
    }

    /**
     * (pozn.: volána pouze jednou, při stavbě mixinové komponenty náležící dané instanci {@link IListeneredTouchConsumer})
     *
     * @return Zda mají být přidány listenery vyvolávající událost <code>click</code> při spuštění události <code>touchDown</code>
     * a událost <code>unclick</code> dojde-li kdekoliv na obrazovce k událost <code>touchUp</code> náležící zakliknutému kurzoru
     * (takový je kanonický výklad významu metod <code>clicked</code>, <code>unclicked</code>)
     * */
    default boolean __ListeneredTouchConsumer_option__shouldClickListenersBeAdded(){return true;}


    /**
     * Factory na {@link EventDelegate}, které budou sloužit jako handlery vstupních událostí.
     * Přepište, pokud chcete v potomkovi použít jinou než kanonickou implementaci {@link EventDelegate}.
     *
     * @return Nová instance {@link EventDelegate}
     * */
    default <T> EventDelegate<T> __ListeneredTouchConsumer_option__MakeEventDelegate(){return EventDelegate.make();}

    /**
     * @return <code>null</code> pokud má být příjímán veškerý vstup z obrazovky, nezávisle na pozici, na které k události došlo,
     *  jinak funkce, která pro každý Delegát náležící jedné ze vstupních událostí vygeneruje funkci generující hranice, za kterými když
     *  ke vstupu dojde, má být událost bez reakce přeskočena; V základní implementaci vygeneruje pro libovolnou instanci {@link Drawable},
     *  jejíž rozměry jsou udávané alespon 2D vektorem se skaláry typu {@link Number}
     *  hranice odpovídající přesně rozměrům daného objektu v prvních 2 dimenzích převedeným na celočíselné hodnoty.
     * */
    default BiFunction<IListeneredTouchConsumer.Util.Impl,EventDelegate<? extends OnMouseMovedArgs>, Function<OnMouseMovedArgs,Pair<Vect2i, Vect2i>>> __ListeneredTouchConsumer_option__touchInputBoundsGetter(){
        if (!(this instanceof Drawable<?, ?>))
            return null;

        Drawable drw = ((Drawable) this);
        Supplier<VectUtil> Pos = drw::getVectUtil;
        Supplier<?> Dim = drw::getSize;

        Function<OnMouseMovedArgs, Pair<Vect2i, Vect2i>> ret = null;

        if (Vect2i.class.isAssignableFrom(Pos.get().getVectClass())) {
            Supplier<Vect2i> dim = ((Supplier) Dim);
            ret = a -> Pair.make(Vect2i.ZERO, dim.get());
        }
        else if (Vect2f.class.isAssignableFrom(Pos.get().getVectClass())) {
            Supplier<Vect2f> dim = ((Supplier) Dim);
            ret = a -> Pair.make(Vect2i.ZERO, dim.get().toInt());
        }
        else if (Vect2d.class.isAssignableFrom(Pos.get().getVectClass())) {
            Supplier<Vect2d> dim = ((Supplier) Dim);
            ret = a -> Pair.make(Vect2i.ZERO, dim.get().toInt());
        }
        else if (Pos.get().DIMENSION_COUNT() >= 2 && Number.class.isAssignableFrom(Pos.get().getScalarClass())) {
            Supplier<?> dim = Dim;
            Supplier<VectUtil<Object, ? extends Number>> pos = ((Supplier) Pos);
            ret = a -> Pair.make(Vect2i.ZERO, Vect2i.make(pos.get().getNth(dim.get(), 0).intValue(), pos.get().getNth(dim.get(), 1).intValue()));
        }

        if (ret != null) {
            Function<OnMouseMovedArgs, Pair<Vect2i, Vect2i>> Ret = ret;
            return (a, b) -> Ret;
        }
        return null;
    }


    /**
     * Statická třída obsahující privátní atributy tohoto Mixin-Rozhraní (Interface v Javě z nějakého důvodu nesmí mít privátní atributy).
     *
     * @author MarkusSecundus
     * */
    static final class Util {
        private static final Function<Extensible, Impl> INSTANCE_SUPPLIER = self->((IListeneredTouchConsumer)self).__ListeneredTouchConsumer__MakeInstance();

        /**
         * (pozn.: V žádném případě nesmí být volána v rámci konfiguračních metod již tázané mixinové komponenty.)
         *
         * @return Mixinová komponenta příslušící dané instanci {@link IListeneredTouchConsumer}
         * */
        protected static Impl getImpl(IListeneredTouchConsumer self){
            return self.getExtension(Util.Impl.class, INSTANCE_SUPPLIER);
        }

        /**
         * Hodnota, kterou může funkce získaná z metody <code>__ListeneredTouchConsumer_option__touchInputBoundsGetter</code>
         * vrátit, aby posuzovaná vstupní událost byla v každém případě přeskočena.
         * */
        public static final Pair<Vect2i, Vect2i> SKIPPER_NO_HIT = Pair.dummy();
        /**
         * Hodnota, kterou může funkce získaná z metody <code>__ListeneredTouchConsumer_option__touchInputBoundsGetter</code>
         * vrátit, aby posuzovaná vstupní událost byla v každém případě přijata.
         * */
        public static final Pair<Vect2i, Vect2i> SKIPPER_ALWAYS_HIT = Pair.dummy();

        /**
         * Implementace a datový kontejner vnitřní mixinové komponenty, na kterou je rozhraní přesměrováváno.
         *
         * @author MarkusSecundus
         * */
        public static class Impl{
            /**
             * Vytvoří instanci mixinové komponenty pro danou instanci {@link IListeneredScrollConsumer}.
             * */
            public Impl(IListeneredTouchConsumer self){
                onTouchDown = new SimpleConstProperty<>(self.__ListeneredTouchConsumer_option__MakeEventDelegate());
                onTouchUp = new SimpleConstProperty<>(self.__ListeneredTouchConsumer_option__MakeEventDelegate());
                onTouchDragged = new SimpleConstProperty<>(self.__ListeneredTouchConsumer_option__MakeEventDelegate());
                onMouseMoved = new SimpleConstProperty<>(self.__ListeneredTouchConsumer_option__MakeEventDelegate());
                onClicked = new SimpleConstProperty<>(self.__ListeneredTouchConsumer_option__MakeEventDelegate());
                onUnclicked = new SimpleConstProperty<>(self.__ListeneredTouchConsumer_option__MakeEventDelegate());

                addBoundsChecker(self);
                addClickListeners(self);
            }

            private void addClickListeners(IListeneredTouchConsumer self){
                if(self.__ListeneredTouchConsumer_option__shouldClickListenersBeAdded()){

                    EventListener<OnTouchArgs> clickApplierOnTouchDown = self::clicked;    //zavolá onClicked na konci onTouchDown
                    onTouchDown.get()._getPostUtilListeners().add(clickApplierOnTouchDown);
                    this.clickApplier = clickApplierOnTouchDown;

                    this.onClickBegin = e->{                                         //na začátku kliknutí - pozanamená v InputManagerovi, aby mi zrušil kliknutost, když se kdekoliv odklikne mně náležející kurzor
                        if(e.manager==null)
                            return true;

                        if(this.isClicked && this.pointer == e.pointer && this.manager == e.manager)
                            return true;

                        if(this.isClicked && untouchOnCursorUpApplier !=null && manager != null)
                            manager.getOnTouchUpListener()._getUtilListeners().remove(untouchOnCursorUpApplier);

                        this.isClicked = true;
                        final int pointer = this.pointer = e.pointer;
                        final InputManager manager = this.manager = e.manager;

                        this.untouchOnCursorUpApplier = args->{
                            if(args.pointer == pointer) {
                                this.untouchOnCursorUpApplier = null;
                                self.unclicked(args.with(self, -1, -1));
                                throw EventDelegate.DELETE_SELF;
                            }
                            return true;
                        };
                        manager.getOnTouchUpListener()._getUtilListeners().add(untouchOnCursorUpApplier);

                        return true;
                    };
                    this.onClicked.get()._getUtilListeners().add(this.onClickBegin);

                    this.onUnclickCleaner = o->{                                   //uklízí po odkliknutí
                        this.isClicked = false;
                        if(this.untouchOnCursorUpApplier !=null)
                            this.manager.getOnTouchUpListener()._getUtilListeners().remove(this.untouchOnCursorUpApplier);
                        return true;
                    };
                    this.onUnclicked.get()._getUtilListeners().add(this.onUnclickCleaner);
                }
            }

            private boolean isClicked = false;
            private int pointer = -1;
            private EventListener<OnTouchArgs> untouchOnCursorUpApplier = null;
            private InputManager manager = null;

            private EventListener<OnTouchArgs> onClickBegin = null;
            private EventListener<Object> onUnclickCleaner = null;
            private EventListener<OnTouchArgs> clickApplier = null;

            /**
             * @return Zda je daný {@link IListeneredTouchConsumer} aktuálně zakliknutý.
             * */
            public boolean isClicked(){return isClicked;}
            /**
             * @return Zda je daný {@link IListeneredTouchConsumer} aktuálně zakliknutý daným kurzorem.
             * */
            public boolean isPointer(int pointer){return this.pointer == pointer;}


            private Map<EventDelegate<? extends OnMouseMovedArgs>, EventListener<OnMouseMovedArgs>>
                    touchSkippers = new HashMap<>();

            private void addBoundsChecker(IListeneredTouchConsumer self){
                BiFunction<IListeneredTouchConsumer.Util.Impl,EventDelegate<? extends OnMouseMovedArgs>,Function<OnMouseMovedArgs,Pair<Vect2i, Vect2i>>>
                        boundsGetter = self.__ListeneredTouchConsumer_option__touchInputBoundsGetter();
                if(boundsGetter!=null){

                    addSkipper(onTouchDown.get(), boundsGetter);
                    addSkipper(onTouchUp.get(), boundsGetter);
                    addSkipper(onTouchDragged.get(), boundsGetter);
                    addSkipper(onMouseMoved.get(), boundsGetter);
                }
            }
            private void addSkipper(EventDelegate<? extends OnMouseMovedArgs> del, BiFunction<IListeneredTouchConsumer.Util.Impl,EventDelegate<? extends OnMouseMovedArgs>, Function<OnMouseMovedArgs,Pair<Vect2i, Vect2i>>> sup){
                Function<OnMouseMovedArgs,Pair<Vect2i, Vect2i>> Sup = sup.apply(this, del);
                if(Sup==null)
                    return;

                EventListener<OnMouseMovedArgs> skipper = e->{
                    Pair<Vect2i, Vect2i> bounds = Sup.apply(e);

                    if(bounds==null || bounds==SKIPPER_ALWAYS_HIT)return true;
                    if(bounds==SKIPPER_NO_HIT)return false;

                    Vect2i min = bounds.first(), max = bounds.second();
                    return e.x>=min.x && e.y>=min.y && e.x<max.x && e.y<max.y;
                };
                del.getListeners(ListenerPriorities.ARG_GUARD).add(0, skipper);
                touchSkippers.put(del, skipper);
            }

            /**
             *  Delegát zpracovávající volání <code>touchDown</code>.
             * */
            public final ConstProperty<EventDelegate<OnTouchArgs>> onTouchDown;
            /**
             *  Delegát zpracovávající volání <code>touchUp</code>.
             * */
            public final ConstProperty<EventDelegate<OnTouchArgs>> onTouchUp;
            /**
             *  Delegát zpracovávající volání <code>touchDragged</code>.
             * */
            public final ConstProperty<EventDelegate<OnTouchDraggedArgs>> onTouchDragged;
            /**
             *  Delegát zpracovávající volání <code>mouseMoved</code>.
             * */
            public final ConstProperty<EventDelegate<OnMouseMovedArgs>> onMouseMoved;

            /**
             *  Delegát zpracovávající volání <code>clicked</code>.
             * */
            public final ConstProperty<EventDelegate<OnTouchArgs>> onClicked;
            /**
             *  Delegát zpracovávající volání <code>unclicked</code>.
             * */
            public final ConstProperty<EventDelegate<OnTouchArgs>> onUnclicked;

        }
    }



    /**
     * Odvozené rozhraní, které navíc poskytuje funkcionalitu pro distribuci dotykových událostí
     * mezi potomky {@link UberDrawable}, jímž by měla být instance implementující tuto variantu rozhraní.
     *
     *
     * @see UberDrawable
     * @see com.markussecundus.forms.elements.impl.layouts.BasicAbstractLayout
     *
     * @author MarkusSecundus
     * */
    public static interface ForLayout extends IListeneredTouchConsumer {

        /**
         * @return <code>null</code> pokud distribuce do potomků nemá být implementována, jinak funkci, která
         *  z aktuální pozice relativní vůči bázi layoutu vygeneruje pozici relativní vůči bázi potomka;
         *  Defaultní implementace poskytnuta pro libovolné {@link UberDrawable} používající alespon 2D
         *  vektor, jehož Skalární složky jsou kompatibilní s typem {@link Number}.
         * */
        public default BiFunction<Vect2i, DrawableElem<?,?>, Vect2i> __ListeneredTouchConsumer_util__childPosTransformer(){
            if(this instanceof UberDrawable){
                UberDrawable self = ((UberDrawable)this);
                VectUtil POS = self.getVectUtil();
                if(Vect2f.class.isAssignableFrom(POS.getVectClass()))
                    return (v, c)-> v.sub(((Vect2f)self.getChildPosition(c)).toInt());
                else if(Vect2i.class.isAssignableFrom(POS.getVectClass()))
                    return (v,c)-> v.sub((Vect2i)self.getChildPosition(c));
                else if(Number.class.isAssignableFrom(POS.getScalarClass())){
                    return (v,c)->{
                        Object childPos = self.getChildPosition(c);
                        return v.sub(Vect2i.make(((Number)POS.getNth(childPos, 0)).intValue(), ((Number)POS.getNth(childPos, 1)).intValue()));
                    };
                }
            }
            return null;
        }

        @Override
        default IListeneredTouchConsumer.Util.Impl __ListeneredTouchConsumer__MakeInstance() {
            if(this instanceof UberDrawable){
                BiFunction<Vect2i, DrawableElem<?,?>, Vect2i> func = __ListeneredTouchConsumer_util__childPosTransformer();
                if(func!=null)
                    return new Util.Impl(this, func);
            }
            return IListeneredTouchConsumer.super.__ListeneredTouchConsumer__MakeInstance();
        }


        /**
         * Statická třída obsahující privátní atributy tohoto Mixin-Rozhraní (Interface v Javě z nějakého důvodu nesmí mít privátní atributy).
         *
         * @author MarkusSecundus
         * */
        static class Util{

            /**
             * (pozn.: V žádném případě nesmí být volána v rámci konfiguračních metod již tázané mixinové komponenty.)
             *
             * @return Mixinová komponenta příslušící dané instanci {@link IListeneredTouchConsumer.ForLayout}
             * */
            public static Impl getImpl(IListeneredTouchConsumer.ForLayout instance){
                return (Impl) IListeneredTouchConsumer.Util.getImpl(instance);
            }


            public static class Impl extends IListeneredTouchConsumer.Util.Impl{
                /**
                 * Vytvoří instanci mixinové komponenty pro danou instanci {@link IListeneredTouchConsumer.ForLayout}.
                 * */
                public Impl(IListeneredTouchConsumer.ForLayout self, BiFunction<Vect2i, DrawableElem<?,?>, Vect2i> childPosTransform){
                    super(self);

                    distrTouchUp = e->distrToChildren(self, e,  childPosTransform, (c,v)->c.touchUp(e.with(c, v.x, v.y)));
                    distrTouchDown = e->distrToChildren(self, e,  childPosTransform, (c,v)->c.touchDown(e.with(c, v.x, v.y)));
                    distrTouchDragged = e->distrToChildren(self, e,  childPosTransform, (c,v)->c.touchDragged(e.with(c, v.x, v.y)));
                    distrMouseMoved = e->distrToChildren(self, e,  childPosTransform, (c,v)->c.mouseMoved(e.with(c, v.x, v.y)));

                    onTouchUp.get()._getPostUtilListeners().add(distrTouchUp);
                    onTouchDown.get()._getPostUtilListeners().add(distrTouchDown);
                    onTouchDragged.get()._getPostUtilListeners().add(distrTouchDragged);
                    onMouseMoved.get()._getPostUtilListeners().add(distrMouseMoved);
                }
                /**
                 *  Delegát zpracovávající distribuci <code>touchUp</code> mezi potomky Layoutu.
                 * */
                public final EventListener<OnTouchArgs> distrTouchUp;
                /**
                 *  Delegát zpracovávající distribuci <code>touchDown</code> mezi potomky Layoutu.
                 * */
                public final EventListener<OnTouchArgs> distrTouchDown;
                /**
                 *  Delegát zpracovávající distribuci <code>touchDragged</code> mezi potomky Layoutu.
                 * */
                public final EventListener<OnTouchDraggedArgs> distrTouchDragged;
                /**
                 *  Delegát zpracovávající distribuci <code>mouseMoved</code> mezi potomky Layoutu.
                 * */
                public final EventListener<OnMouseMovedArgs> distrMouseMoved;

                private static boolean distrToChildren(IListeneredTouchConsumer.ForLayout self, OnMouseMovedArgs v, BiFunction<Vect2i, DrawableElem<?,?>, Vect2i> childPosTransform, BiPredicate<InputConsumer, Vect2i> distr){
                    boolean ret = false;
                    for(DrawableElem<?,?> child: ((UberDrawable<?,?>) self).getDrawableChildren())
                        ret |= (child instanceof InputConsumer && distr.test((InputConsumer) child, childPosTransform.apply(Vect2i.make(v.x, v.y), child)));

                    return ret;
                }
            }
        }
    }
}
