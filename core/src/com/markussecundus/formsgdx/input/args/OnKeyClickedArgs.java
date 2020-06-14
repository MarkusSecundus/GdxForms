package com.markussecundus.formsgdx.input.args;

import com.badlogic.gdx.InputProcessor;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.InputManager;

/**
 * Datová třída reprezentující argument pro funkce
 * <code>onKeyDown</code>, <code>onKeyUp</code> rozhraní {@link InputConsumer}.
 *
 * @see OnKeyTypedArgs
 *
 *
 * @see OnInputEventArgs
 *
 * @see InputConsumer
 * @see InputManager
 *
 * @see OnMouseMovedArgs
 * @see OnTouchDraggedArgs
 * @see OnTouchArgs
 *
 * @see OnScrolledArgs
 *
 * @author MarkusSecundus
 * */
public class OnKeyClickedArgs extends OnInputEventArgs {

    /**
     * Stejný význam jako hodnota <code>keycode</code> předávaná metodě
     * <code>onKeyDown</code> / <code>onKeyUp</code>
     * na LibGDXím rozhraní {@link InputProcessor}.
     * */
    public final int keycode;

    /**
     * Inicializuje instanci danými hodnotami.
     * */
    protected OnKeyClickedArgs(InputManager manager, InputConsumer actor, int keycode){
        super(manager, actor);
        this.keycode = keycode;
    }

    /**
     * @return Instance datové třídy sestávající z daných hodnot.
     * */
    public static OnKeyClickedArgs make(InputManager manager, InputConsumer actor, int keycode){
        return new OnKeyClickedArgs(manager, actor, keycode);
    }

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnKeyClickedArgs with(InputConsumer actor){return actor==this.actor?this:make(manager, actor, keycode);}

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnKeyClickedArgs with(InputConsumer actor, int keycode){return actor==this.actor?this:make(manager, actor, keycode);}
}
