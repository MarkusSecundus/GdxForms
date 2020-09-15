package com.markussecundus.formsgdx.input.args;

import com.badlogic.gdx.InputProcessor;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.InputManager;

/**
 * Datová třída reprezentující argument pro funkce <code>onTouchDown</code>, <code>onTouchUp</code> rozhraní {@link InputConsumer}.
 *
 * @see OnMouseMovedArgs
 * @see com.markussecundus.formsgdx.input.args.OnTouchDraggedArgs
 *
 * @see OnInputEventArgs
 *
 * @see InputConsumer
 * @see InputManager
 *
 * @see OnKeyClickedArgs
 * @see OnKeyTypedArgs
 *
 * @see OnScrolledArgs
 *
 * @author MarkusSecundus
 * */
public class OnTouchArgs extends OnTouchDraggedArgs {
    /**
     * Stejný význam jako hodnota <code>button</code> předávaná
     * metodě <code>onTouchDown</code> / <code>onTouchUp</code>
     * na LibGDXím rozhraní {@link InputProcessor}.
     * */
    public final int button;

    /**
     * Inicializuje instanci danými hodnotami.
     * */
    protected OnTouchArgs(InputManager manager, InputConsumer actor, int x, int y, int pointer, int button){
        super(manager, actor, x, y, pointer);
        this.button = button;
    }

    /**
     * @return Instance datové třídy sestávající z daných hodnot.
     * */
    public static OnTouchArgs make(InputManager manager, InputConsumer actor, int x, int y, int pointer, int button){
        return new OnTouchArgs(manager, actor, x,y, pointer, button);
    }

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnTouchArgs with(InputConsumer actor){return actor==this.actor?this:make(manager, actor, x, y,pointer, button);}

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnTouchArgs with(InputConsumer actor, int x, int y){return make(manager, actor, x, y,pointer, button);}

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnTouchArgs with(InputConsumer actor, int x, int y, int pointer){return make(manager, actor, x, y,pointer, button);}
}
