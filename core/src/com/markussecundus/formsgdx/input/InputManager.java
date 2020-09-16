package com.markussecundus.formsgdx.input;

import com.badlogic.gdx.Gdx;
import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.extensibility.IExtensible;
import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.utils.function.BiFunction;
import com.markussecundus.forms.utils.function.BiPredicate;
import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.utils.function.Predicate;
import com.markussecundus.forms.utils.datastruct.ObservedList;
import com.markussecundus.forms.utils.vector.Vect2i;
import com.markussecundus.formsgdx.input.args.OnMouseMovedArgs;
import com.markussecundus.formsgdx.input.args.OnTouchArgs;
import com.markussecundus.formsgdx.input.mixins.IListeneredTouchConsumer;
import com.markussecundus.formsgdx.input.mixins.IListeneredUniversalConsumer;

import java.util.ArrayList;
import java.util.List;



/**
 * Objekt starající se o příjem vstupních událostí a jejich distribuci mezi konzumenty, jež
 * jsou u něj zaregistrováni.
 *
 * @see InputConsumer
 *
 * @author MarkusSecundus
 * */
public class InputManager extends IExtensible implements  InputConsumer, IListeneredUniversalConsumer {

    /**
     * @return List obsahující všechny konzumenty zaregistrované na příjem událostí z klávesnice,
     *      přidáním sebe do něj se k nim lze zaregistrovat atd.
     * */
    public List<InputConsumer> getKeyConsumers(){return keyConsumers;}
    /**
     * @return List obsahující všechny konzumenty zaregistrované na příjem událostí z myši / dotykové obrazovky,
     *      přidáním sebe do něj se k nim lze zaregistrovat atd.
     * */
    public List<InputConsumer> getTouchConsumers(){return touchConsumers;}
    /**
     * @return List obsahující všechny konzumenty zaregistrované na příjem událostí kolečka myši,
     *      přidáním sebe do něj se k nim lze zaregistrovat atd.
     * */
    public List<InputConsumer> getScrollConsumers(){return scrollConsumers;}

    /**
     * Inicializuje instanci a přidá do jejích Delegátů listenery pro distribuci vstupních událostí
     * mezi registrované příjemce.
     * */
    public InputManager(){
        getOnKeyDownListener()._getPostUtilListeners()
                .add(e-> tryConsumers(getKeyConsumers(), c->c.keyDown(e.with(c))));
        getOnKeyUpListener()._getPostUtilListeners()
                .add(e->tryConsumers(getKeyConsumers(), c->c.keyUp(e.with(c))));
        getOnKeyTypedListener()._getPostUtilListeners()
                .add(e->tryConsumers(getKeyConsumers(), c->c.keyTyped(e.with(c))));


        getOnTouchDownListener()._getPostUtilListeners()
                .add(e->tryConsumers(getTouchConsumers(), e.x, e.y, (c,v)->c.touchDown(e.with(c, v.x, v.y))));
        getOnTouchUpListener()._getPostUtilListeners()
                .add(e->tryConsumers(getTouchConsumers(), e.x, e.y, (c,v)->c.touchUp(e.with(c, v.x, v.y))));
        getOnTouchDraggedListener()._getPostUtilListeners()
                .add(e->tryConsumers(getTouchConsumers(), e.x, e.y, (c,v)->c.touchDragged(e.with(c, v.x, v.y))));
        getOnMouseMovedListener()._getPostUtilListeners()
                .add(e->tryConsumers(getTouchConsumers(), e.x, e.y, (c,v)->c.mouseMoved(e.with(c, v.x, v.y))));

        getOnClickedListener()._getPostUtilListeners()
                .add(e->tryConsumers(getTouchConsumers(), e.x, e.y, (c,v)->c.clicked(e.with(c, v.x, v.y))));
        getOnUnclickedListener()._getPostUtilListeners()
                .add(e->tryConsumers(getTouchConsumers(), e.x, e.y, (c,v)->c.clicked(e.with(c, v.x, v.y))));


        getOnScrollListener()._getPostUtilListeners()
                .add(e->tryConsumers(getScrollConsumers(), c->c.scrolled(e.with(c, e.amount))));
    }

    /**
     * Funkce transformující souřadnice vstupní události získané z vnějšího zdroje
     * na souřadnice k distribuci příjemcům.
     * */
    public static interface CoordsTransform{
        /**
         * @param x souřadnice získaná z vnějšího zdroje vstupní události
         * @param y souřadnice získaná z vnějšího zdroje vstupní události
         *
         * @return souřadnice jak mají být prezentovány příjemcům
         * */
        public Vect2i transform(int x, int y);
    }

    /**
     * Funkce transformující souřadnice vstupní události získané z vnějšího zdroje
     * na souřadnice k distribuci příjemcům.
     *
     * Defaultní implementace zachovává souřadnici x a souřadnici y odečítá od LibGDXem detekované výšky obrazovky,
     * jak je to nutné při příjmu z {@link com.badlogic.gdx.InputProcessor}.
     * */
    public CoordsTransform coordsTransform = (x,y)->Vect2i.make(x, Gdx.graphics.getHeight()-y);


    private boolean tryConsumers(Iterable<InputConsumer> consumers, int screenX, int screenY, BiPredicate<InputConsumer, Vect2i> fnc){
        Vect2i coords = coordsTransform.transform(screenX, screenY);
        for(InputConsumer consumer: consumers) {
            if (fnc.test(consumer, coords))
                return true;
        }
        return true;
    }
    private boolean tryConsumers(Iterable<InputConsumer> consumers, Predicate<InputConsumer> fnc){
        for(InputConsumer consumer: consumers) {
            if (fnc.test(consumer))
                return true;
        }
        return true;
    }

    /**
     * Listenery pro zakliknutí a odkliknutí {@link InputManager}a nebudou přidány.
     *
     * @return <code>false</code>
     * */
    @Override
    public boolean __ListeneredTouchConsumer_option__shouldClickListenersBeAdded() {
        return false;
    }

    /**
     * Přijat bude libovolný vstup detekovaný na jakémkoliv místě obrazovky.
     *
     * @return <code>null</code>
     * */
    @Override
    public BiFunction<IListeneredTouchConsumer.Util.Impl,EventDelegate<? extends OnMouseMovedArgs>, Function<OnMouseMovedArgs,Pair<Vect2i, Vect2i>>> __ListeneredTouchConsumer_option__touchInputBoundsGetter() {
        return null;
    }

    private final List<InputConsumer> keyConsumers = new ArrayList<>();
    private final List<InputConsumer> scrollConsumers = new ArrayList<>();
    private final List<InputConsumer> touchConsumers = new ObservedList.Blank<InputConsumer>(new ArrayList<>()){
        @Override protected void onDelete(Object t) {
            if(t instanceof InputConsumer && ((InputConsumer)t).isClicked())
                ((InputConsumer)t).unclicked(OnTouchArgs.make(InputManager.this, ((InputConsumer)t), -1, -1, -1, -1));
        }
        @Override protected void onSet(InputConsumer oldElem, InputConsumer newElem, int index) {
            if(oldElem!=newElem)
                onDelete(oldElem);
        }
    };


    /**
     * Varianta schopná přijímat vstup skrze rozhraní {@link com.badlogic.gdx.InputProcessor}.
     *
     *     Lze ji tedy zaregistrovat na přijímání vstupu ze skutečných periferií
     *     voláním '<code>Gdx.input.setInputProcessor(this)</code>'.
     * */
    public static class AsInputProcessor extends InputManager implements InputConsumer.ToInputProcessor {
        /**
         * Inicializuje instanci a přidá do jejích Delegátů listenery pro distribuci vstupních událostí
         * mezi registrované příjemce.
         * */
        public AsInputProcessor(){super();}

        /**
         * Jako {@link com.markussecundus.formsgdx.input.InputManager} iniciující vstupní událost bude považován on sám.
         *
         * @return <code>this</code>
         * */
        @Override
        public InputManager __InputConsumerToProcessor_getInputManager() { return this; }
    }
}
