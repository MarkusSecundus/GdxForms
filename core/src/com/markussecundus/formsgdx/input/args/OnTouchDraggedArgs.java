package com.markussecundus.formsgdx.input.args;

import com.badlogic.gdx.InputProcessor;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.InputManager;

/**
 * Datová třída reprezentující argument pro funkci <code>onTouchDragged</code> rozhraní {@link InputConsumer}.
 *
 * @see OnMouseMovedArgs
 * @see OnTouchArgs
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
public class OnTouchDraggedArgs extends OnMouseMovedArgs {
    /**
     * Identifikátor prstu, přes který byl dotyk proveden (má smysl u multitouch zařízení).
     *
     * Stejný význam jako hodnota <code>pointer</code> předávaná
     * metodě <code>onTouchDragged</code> na LibGDXím rozhraní {@link InputProcessor}.
     * */
    public final int pointer;

    /**
     * Inicializuje instanci danými hodnotami.
     * */
    protected OnTouchDraggedArgs(InputManager manager, InputConsumer actor, int x, int y, int pointer){
        super(manager, actor, x, y);
        this.pointer = pointer;
    }

    /**
     * @return Instance datové třídy sestávající z daných hodnot.
     * */
    public static OnTouchDraggedArgs make(InputManager manager,InputConsumer actor, int x, int y, int pointer){
        return new OnTouchDraggedArgs(manager, actor, x,y, pointer);
    }


    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnTouchDraggedArgs with(InputConsumer actor){return actor==this.actor?this:make(manager, actor, x, y,pointer);}

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnTouchDraggedArgs with(InputConsumer actor, int x, int y){return make(manager, actor, x, y,pointer);}

}
