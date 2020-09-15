package com.markussecundus.formsgdx.input;

import com.badlogic.gdx.InputProcessor;
import com.markussecundus.formsgdx.input.interfaces.ListeneredKeyConsumer;
import com.markussecundus.formsgdx.input.interfaces.ListeneredScrollConsumer;
import com.markussecundus.formsgdx.input.interfaces.ListeneredTouchConsumer;
import com.markussecundus.formsgdx.input.interfaces.ListeneredUniversalConsumer;
import com.markussecundus.formsgdx.input.args.OnKeyClickedArgs;
import com.markussecundus.formsgdx.input.args.OnKeyTypedArgs;
import com.markussecundus.formsgdx.input.args.OnMouseMovedArgs;
import com.markussecundus.formsgdx.input.args.OnScrolledArgs;
import com.markussecundus.formsgdx.input.args.OnTouchArgs;
import com.markussecundus.formsgdx.input.args.OnTouchDraggedArgs;
import com.markussecundus.formsgdx.input.mixins.IListeneredTouchConsumer;


/**
 * Rozhraní pro objekty reagující na vstup z klávesnice a myši / dotykové obrazovky.
 * <p></p>
 * Robustnější a s {@link com.markussecundus.forms.events.EventDelegate}s lépe sladitelná obdoba standardního LibGDXího {@link InputProcessor}.
 *
 * @see InputProcessor
 * @see InputManager
 *
 * @see ListeneredTouchConsumer
 * @see ListeneredScrollConsumer
 * @see ListeneredKeyConsumer
 * @see ListeneredUniversalConsumer
 *
 *
 * @author MarkusSecundus
 * */
public interface InputConsumer {

    /**
     * Odpovídá <code>keyDown</code> LibGDXího {@link InputProcessor}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean keyDown(OnKeyClickedArgs e);
    /**
     * Odpovídá <code>keyUp</code> LibGDXího {@link InputProcessor}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean keyUp(OnKeyClickedArgs e) ;
    /**
     * Odpovídá <code>keyTyped</code> LibGDXího {@link InputProcessor}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean keyTyped(OnKeyTypedArgs e) ;

    /**
     * Odpovídá <code>touchDown</code> LibGDXího {@link InputProcessor}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean touchDown(OnTouchArgs e);
    /**
     * Odpovídá <code>touchUp</code> LibGDXího {@link InputProcessor}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean touchUp(OnTouchArgs e);
    /**
     * Odpovídá <code>touchDragged</code> LibGDXího {@link InputProcessor}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean touchDragged(OnTouchDraggedArgs e) ;
    /**
     * Odpovídá <code>mouseMoved</code> LibGDXího {@link InputProcessor}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean mouseMoved(OnMouseMovedArgs e);

    /**
     * Odpovídá <code>scrolled</code> LibGDXího {@link InputProcessor}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean scrolled(OnScrolledArgs e);

    /**
     * Voláno když je na objekt kliknuto.
     *
     * Má volnou definici, pro kanonický výklad viz {@link IListeneredTouchConsumer}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean clicked(OnTouchArgs e);

    /**
     * Voláno když je objekt odkliknut.
     *
     * Má volnou definici, pro kanonický výklad viz {@link IListeneredTouchConsumer}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean unclicked(OnTouchArgs e);

    /**
     * @return Zda je objekt aktuálně zakliknutý (pro definici zakliknutosti viz {@link IListeneredTouchConsumer})
     * */
    public default boolean isClicked(){return false;}




    /**
     * Wrapper sloužící k obalení instance {@link InputProcessor} do rozhraní kompatibilního s {@link com.markussecundus.formsgdx.input.InputConsumer}.
     *
     * @see InputProcessor
     * @see InputConsumer
     *
     * @author MarkusSecundus
     * */
    @FunctionalInterface
    public static interface FromInputProcessor extends InputConsumer {
        /**
         * Vytvoří instanci nad danou bází.
         * */
        public static com.markussecundus.formsgdx.input.InputConsumer.FromInputProcessor make(InputProcessor base){return ()->base;}

        /**
         * Instance {@link InputProcessor} na kterou wrapper ukazuje.
         * */
        public InputProcessor __InputConsumerFromProcessor_getBase();

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public default boolean keyDown(OnKeyClickedArgs e) { return __InputConsumerFromProcessor_getBase().keyDown(e.keycode); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public default boolean keyUp(OnKeyClickedArgs e) { return __InputConsumerFromProcessor_getBase().keyUp(e.keycode); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public default boolean keyTyped(OnKeyTypedArgs e) { return __InputConsumerFromProcessor_getBase().keyTyped(e.character); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public default boolean touchDown(OnTouchArgs e) { return __InputConsumerFromProcessor_getBase().touchDown(e.x, e.y, e.pointer, e.button); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public default boolean touchUp(OnTouchArgs e) { return __InputConsumerFromProcessor_getBase().touchUp(e.x, e.y, e.pointer,e.button); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public default boolean touchDragged(OnTouchDraggedArgs e) { return __InputConsumerFromProcessor_getBase().touchDragged(e.x,e.y, e.pointer); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public default boolean mouseMoved(OnMouseMovedArgs e) { return __InputConsumerFromProcessor_getBase().mouseMoved(e.x,e.y); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public default boolean scrolled(OnScrolledArgs e) { return __InputConsumerFromProcessor_getBase().scrolled(e.amount); }

        /**
         * Nedělá nic.
         *
         * @return <code>false</code> (vždy)
         * */
        @Override
        public default boolean clicked(OnTouchArgs e) { return false; }

        /**
         * Nedělá nic.
         *
         * @return <code>false</code> (vždy)
         * */
        @Override
        public default boolean unclicked(OnTouchArgs e) { return false; }

        /**
         * Nedělá nic.
         *
         * @return <code>false</code> (vždy)
         * */
        @Override
        public default boolean isClicked() { return false; }
    }


    /**
     * Mixin-rozhraní, které {@link InputConsumer}ovi, který ho implementuje, přidá funkcionalitu získávání vstupu
     * jako instance standardního LibGDXího {@link InputProcessor}.
     *
     * @see InputProcessor
     * @see InputManager
     * @see InputConsumer
     *
     *
     * @author MarkusSecundus
     * */
    public static interface ToInputProcessor extends InputConsumer, InputProcessor {
        /**{@inheritDoc}*/
        @Override
        default boolean keyDown(int keycode){
            return this.keyDown(OnKeyClickedArgs.make(__InputConsumerToProcessor_getInputManager(), this, keycode));
        }

        /**{@inheritDoc}*/
        @Override
        default boolean keyUp(int keycode){
            return this.keyUp(OnKeyClickedArgs.make(__InputConsumerToProcessor_getInputManager(), this, keycode));
        }

        /**{@inheritDoc}*/
        @Override
        default boolean keyTyped(char character){
            return this.keyTyped(OnKeyTypedArgs.make(__InputConsumerToProcessor_getInputManager(), this, character));
        }

        /**{@inheritDoc}*/
        @Override
        default boolean touchDown(int screenX, int screenY, int pointer, int button){
            return this.touchDown(OnTouchArgs.make(__InputConsumerToProcessor_getInputManager(), this, screenX, screenY, pointer, button));
        }

        /**{@inheritDoc}*/
        @Override
        default boolean touchUp(int screenX, int screenY, int pointer, int button){
            return this.touchUp(OnTouchArgs.make(__InputConsumerToProcessor_getInputManager(), this, screenX, screenY, pointer, button));
        }

        /**{@inheritDoc}*/
        @Override
        default boolean touchDragged(int screenX, int screenY, int pointer){
            return this.touchDragged(OnTouchDraggedArgs.make(__InputConsumerToProcessor_getInputManager(), this, screenX, screenY, pointer));
        }

        /**{@inheritDoc}*/
        @Override
        default boolean mouseMoved(int screenX, int screenY){
            return this.mouseMoved(OnMouseMovedArgs.make(__InputConsumerToProcessor_getInputManager(), this, screenX, screenY));
        }

        /**{@inheritDoc}*/
        @Override
        default boolean scrolled(int amount){
            return this.scrolled(OnScrolledArgs.make(__InputConsumerToProcessor_getInputManager(), this, amount));
        }

        /**
         * @return Instance {@link InputManager}, která má být uvažována jako iniciátor vstupní události.
         * */
        InputManager __InputConsumerToProcessor_getInputManager();
    }

}
