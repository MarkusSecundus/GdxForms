package com.markussecundus.formsgdx.input;

import com.badlogic.gdx.InputProcessor;
import com.markussecundus.formsgdx.input.args.OnKeyClickedArgs;
import com.markussecundus.formsgdx.input.args.OnKeyTypedArgs;
import com.markussecundus.formsgdx.input.args.OnMouseMovedArgs;
import com.markussecundus.formsgdx.input.args.OnScrolledArgs;
import com.markussecundus.formsgdx.input.args.OnTouchArgs;
import com.markussecundus.formsgdx.input.args.OnTouchDraggedArgs;


/**
 * Rozhraní pro objekty reagující na vstup z klávesnice a myši / dotykové obrazovky.
 *
 * Robustnější a s {@link com.markussecundus.forms.events.EventDelegate}s lépe sladitelná obdoba standardního LibGDXího {@link InputProcessor}.
 *
 * @see InputProcessor
 * @see InputManager
 *
 * @see com.markussecundus.formsgdx.input.interfaces.ListeneredTouchConsumer
 * @see com.markussecundus.formsgdx.input.interfaces.ListeneredScrollConsumer
 * @see com.markussecundus.formsgdx.input.interfaces.ListeneredKeyConsumer
 * @see com.markussecundus.formsgdx.input.interfaces.ListeneredUniversalConsumer
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
     * Má volnou definici, pro kanonický výklad viz {@link com.markussecundus.formsgdx.input.mixins.IListeneredTouchConsumer}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean clicked(OnTouchArgs e);

    /**
     * Voláno když je objekt odkliknut.
     *
     * Má volnou definici, pro kanonický výklad viz {@link com.markussecundus.formsgdx.input.mixins.IListeneredTouchConsumer}
     *
     * @return Zda byla událost úspěšně přijata a zpracována
     * */
    public boolean unclicked(OnTouchArgs e);

    /**
     * @return Zda je objekt aktuálně zakliknutý (pro definici zakliknutosti viz {@link com.markussecundus.formsgdx.input.mixins.IListeneredTouchConsumer})
     * */
    public default boolean isClicked(){return false;}

    /**
     * @return Instance ze vstupu obalená do {@link InputConsumer} wrapperu.
     * */
    public static FromInputProcessor make(InputProcessor processor){return new FromInputProcessor(processor);}



    /**
     * Wrapper sloužící k obalení instance {@link InputProcessor} do rozhraní kompatibilního s {@link InputConsumer}.
     *
     * @see InputProcessor
     * @see InputConsumer
     *
     * @author MarkusSecundus
     * */
    public static class FromInputProcessor implements InputConsumer{
        /**
         * Vytvoří instanci nad danou bází.
         * */
        public FromInputProcessor(InputProcessor base){this.base=base;}

        /**
         * Instance {@link InputProcessor} na kterou wrapper ukazuje.
         * */
        public final InputProcessor base;

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public boolean keyDown(OnKeyClickedArgs e) { return base.keyDown(e.keycode); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public boolean keyUp(OnKeyClickedArgs e) { return base.keyUp(e.keycode); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public boolean keyTyped(OnKeyTypedArgs e) { return base.keyTyped(e.character); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public boolean touchDown(OnTouchArgs e) { return base.touchDown(e.x, e.y, e.pointer, e.button); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public boolean touchUp(OnTouchArgs e) { return base.touchUp(e.x, e.y, e.pointer,e.button); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public boolean touchDragged(OnTouchDraggedArgs e) { return base.touchDragged(e.x,e.y, e.pointer); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public boolean mouseMoved(OnMouseMovedArgs e) { return base.mouseMoved(e.x,e.y); }

        /**
         * Přesměrovává na bázi.
         *
         * {@inheritDoc}
         * */
        @Override
        public boolean scrolled(OnScrolledArgs e) { return base.scrolled(e.amount); }

        /**
         * Nedělá nic.
         *
         * @return <code>false</code> (vždy)
         * */
        @Override
        public boolean clicked(OnTouchArgs e) { return false; }

        /**
         * Nedělá nic.
         *
         * @return <code>false</code> (vždy)
         * */
        @Override
        public boolean unclicked(OnTouchArgs e) { return false; }

        /**
         * Nedělá nic.
         *
         * @return <code>false</code> (vždy)
         * */
        @Override
        public boolean isClicked() { return false; }
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
