package com.markussecundus.formsgdx.examples;

import com.markussecundus.forms.elements.impl.BasicAbstractDrawable;
import com.markussecundus.forms.elements.impl.utils.DefaultSizeBehavior;
import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.utils.function.BiFunction;
import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.Vect2i;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;
import com.markussecundus.formsgdx.Style;
import com.markussecundus.formsgdx.graphics.RoundedRectangle;
import com.markussecundus.formsgdx.input.args.OnMouseMovedArgs;
import com.markussecundus.formsgdx.input.args.OnTouchDraggedArgs;
import com.markussecundus.formsgdx.input.mixins.IListeneredTouchConsumer;
import com.markussecundus.formsgdx.input.mixins.IListeneredUniversalConsumer;
import com.markussecundus.formsgdx.rendering.BasicRenderer;


/**
 * Demonstrační implementace jednoduchého slideru provedená za pomoci služeb poskytnutých Formulářovými knihovnami.
 *
 *
 *
 * @author MarkusSecundus
 * */
public abstract class Slider< Telo extends GraphicalPrimitive<? super BasicRenderer, Vect2f, Float>, Cudlik extends  GraphicalPrimitive<? super BasicRenderer, Vect2f, Float>>
        extends BasicAbstractDrawable<BasicRenderer, Vect2f, Float> implements IListeneredUniversalConsumer {

    /**
     * Inicializuje rozměry slideru aktuálními rozměry jeho komponent.
     * */
    public Slider(Telo telo, Cudlik cudlik){
        //slider bude v obou rozměrech minimálně stejně velký jako každá z komponent
        this(telo.getDimensions().withFloor(cudlik.getDimensions()), telo, cudlik);
    }

    public Slider(Vect2f prefSize, Telo telo, Cudlik cudlik) {
        super(Vect2f.getUtility(), Vect2f.INF, Vect2f.ZERO, prefSize);

        this.telo = telo;
        this.cudlik = cudlik;


        //<při změně velikosti Slideru zaručíme, že komponenty se do něj pořád ještě vejdou>

        //zaručuje, že komponenta bude mít velikost ve všech rozměrech omezenou celkovou velikostí slideru
        // a při jejím vynuceném zmenšení bude jevit snahu o návrat do původního stavu jakmile může
        //drobná nevýhoda pak je, že změny rozměrů provedené uživatelem přímo na komponentách nebudou mít účinek,
        // ale ošetření této chyby pro stručnost přeskočíme
        DefaultSizeBehavior<Vect2f, Float> telo_behavior = new DefaultSizeBehavior<>(prefSize, Vect2f.ZERO, telo.getDimensions(), Vect2f.getUtility());

        //obdobně
        DefaultSizeBehavior<Vect2f, Float> cudlik_behavior = new DefaultSizeBehavior<>(prefSize, Vect2f.ZERO, cudlik.getDimensions(), Vect2f.getUtility());

        //listenerem svážeme komponenty s DefaultSizeBahavior, které jsme pro ně vytvořili
        this.size().getSetterListeners()._getUtilListeners().add(e->{
            Vect2f size = e.newVal().get(); //právě nastavená velikost Slideru

            telo_behavior.maxSize.set(size);  //omezíme velikost posuvníkové části celkovou aktuální velikostí Slideru
            cudlik_behavior.maxSize.set(size); //obdobně pro čudlík

            telo.setDimensions(telo_behavior.realSize.get());  //nastavíme rozměry, které podle nových hodnot vyplivl DefaultSizeBehavior
            cudlik.setDimensions(cudlik_behavior.realSize.get()); //obdobně u čudlíku
            return true;
        });
        this.prefSize().pretendSet();   //právě přidaný listener rovnou provedeme

        //<\při změně velikosti Slideru zaručíme, že komponenty se do něj pořád ještě vejdou>


        this.value  = new SimpleProperty<>(0f);                 //hodnota posuvníku je iniciálně 0

        this.value.getSetterListeners()._getUtilListeners().add(e->{            //zaručíme, že hodnota posuvníku bude v intervalu 0f..1f, případné přetečení se oseká
            e.newVal().set(FormsUtil.intoBounds(0f,e.newVal().get(), 1f));
            return true;
        });

        this.getOnTouchDraggedListener()._getUtilListeners().add(e->{   //při kliknutí na posuvník nastavíme hodnotu podle polohy kliknutí
            setValue(e.x/getSize().x);           //podíl vzdálenosti kliku od počátku vůči celkové délce posuvníku
            return true;
        });
        this.getOnClickedListener()._getPostUtilListeners().add(e->{    //změna hodnoty posuvníku se provede i při jednorázovém kliknutí
            this.touchDragged(e);
            return true;
        });
    }

    /**
     * Vykreslí posuvník na obrazovku.
     *
     * @param renderer instance, skrze kterou má být vykreslen
     * @param position souřadnice jeho levého dolního rohu
     *
     * {@inheritDoc}
     * */
    @Override
    public void draw(BasicRenderer renderer, Vect2f position) {
        Vect2f dims = getSize(), telo_dims = telo.getDimensions(), cudlik_dims = cudlik.getDimensions();
        telo.draw(renderer, position.addY((dims.y - telo_dims.y)/2) );
        cudlik.draw(renderer, position.add((dims.x - cudlik_dims.x)*getValue(), (dims.y - cudlik_dims.y)/2));
    }

    /**
     * Nedělá nic.
     * {@inheritDoc}
     * */
    @Override public void update(float delta, int frameId) {}

    /**
     * Hodnota na posuvníku, prvek intervalu [0f..1f].
     *
     * @return hodnota posuvníku
     * */
    public Property<Float> value(){return value;}

    /**
     * @return Pohodlnější zkratka pro <code>value().get()</code>
     * */
    public Float getValue(){return value().get();}
    /**
     * @return Pohodlnější zkratka pro <code>value().set(newVal)</code>
     * */
    public Float setValue(Float newVal){return value().set(newVal);}


    /**{@inheritDoc}*/
    @Override
    public VectUtil<Vect2f, Float> getVectUtil() { return Vect2f.getUtility(); }


    /**
     * Budeu zachován defaultní způsob osekávání vstupů pro všechny typy událostí kromě <code>touchDragged</code> -
     *  ta bude odchycena vždy, pokud je provedena prstem, kterým je Slider aktuálně zakliknutý, bez ohledu na to,
     *  kde na obrazovce k ní došlo; pokud je provedena nezamáčknutým prstem, bude naopak vždy přeskočena.
     * */
    @Override
    public BiFunction<IListeneredTouchConsumer.Util.Impl, EventDelegate<? extends OnMouseMovedArgs>, Function<OnMouseMovedArgs,Pair<Vect2i, Vect2i>>> __ListeneredTouchConsumer_option__touchInputBoundsGetter() {

        //získáme výchozí návratovou hodnotu této funkce, abychom na ní mohli dále přesměrovávat
        BiFunction<IListeneredTouchConsumer.Util.Impl, EventDelegate<? extends OnMouseMovedArgs>, Function<OnMouseMovedArgs,Pair<Vect2i, Vect2i>>> puvodni =
                IListeneredUniversalConsumer.super.__ListeneredTouchConsumer_option__touchInputBoundsGetter();

        return (self, del)-> (del == self.onTouchDragged.get())     //pro Delegát zpracovávající onTouchDragged
                ? (e -> self.isClicked() && self.isPointer(((OnTouchDraggedArgs)e).pointer) ? IListeneredTouchConsumer.Util.SKIPPER_ALWAYS_HIT : IListeneredTouchConsumer.Util.SKIPPER_NO_HIT)
                : puvodni.apply(self, del);     //pro ostatní Delegáty vrátíme původní hodnotu

    }

    //private:

    /**
     * Property pro hodnotu slideru.
     */
    private final Property<Float> value;

    /**
     * Posuvníková část slideru.
     * */
    public final Telo telo;

    /**
     * Ikona čudlíku.
     * */
    public final Cudlik cudlik;


    /**
     * Konkrétnější implementace, kde grafická stránka je tvořena zaoblenými obdélníky s obrubou a když je slider zakliknutý,
     * změní jeho čudlík barvu.
     *
     * @see Slider
     * @see RoundedRectangle
     * @see BasicRenderer
     *
     * @author MarkusSecundus
     * */
    public static class Basic extends Slider<BasicRenderer.ShapeToBasicDrw<Vect2f, Float, RoundedRectangle.SObrubou>,BasicRenderer.ShapeToBasicDrw<Vect2f, Float, RoundedRectangle.SObrubou>>{
        /**
         * Vytvoří nový slider, kde barvy obou jeho komponent jsou jednotně dány <code>styl</code>em
         * a jejich velikosti dány zbylými dvěma parametry.
         * */
        public Basic(Style styl, Vect2f teloSize, Vect2f cudlSize){
            super( new BasicRenderer.ShapeToBasicDrw<>(new RoundedRectangle.SObrubou(teloSize, styl)),  new BasicRenderer.ShapeToBasicDrw<>(new RoundedRectangle.SObrubou(cudlSize, styl)));

            //při zakliknutí Slideru změníme barvu čudlíku
            this.getOnClickedListener()._getUtilListeners().add(e->{
                this.cudlik.base.getInner().setColor(styl.transitionColor);
                return true;
            });

            //při odkliknutí Slideru vrátíme čudlíku původní barvu
            this.getOnUnclickedListener()._getUtilListeners().add(e->{
                this.cudlik.base.getInner().setColor(styl.innerColor);
                return true;
            });
        }
    }
}
