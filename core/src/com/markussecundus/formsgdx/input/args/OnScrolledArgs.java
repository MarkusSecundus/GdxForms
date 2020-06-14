package com.markussecundus.formsgdx.input.args;

import com.badlogic.gdx.InputProcessor;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.InputManager;

/**
 * Datová třída reprezentující argument pro funkci <code>onScrolled</code> rozhraní {@link InputConsumer}.
 *
 * @see OnInputEventArgs
 *
 * @see InputConsumer
 * @see InputManager
 *
 * @see OnKeyClickedArgs
 * @see OnKeyTypedArgs
 *
 * @see OnMouseMovedArgs
 * @see OnTouchDraggedArgs
 * @see OnTouchArgs
 *
 * @author MarkusSecundus
 * */
public class OnScrolledArgs extends OnInputEventArgs{
    /**
     * Stejný význam jako hodnota <code>amount</code> předávaná
     * metodě <code>onScrolled</code> na LibGDXím rozhraní {@link InputProcessor}.
     * */
    public final int amount;

    /**
     * Inicializuje instanci danými hodnotami.
     * */
    protected OnScrolledArgs(InputManager manager, InputConsumer actor, int amount){
        super(manager, actor);
        this.amount = amount;
    }


    /**
     * @return Instance datové třídy sestávající z daných hodnot.
     * */
    public static OnScrolledArgs make(InputManager manager, InputConsumer actor, int amount){
        return new OnScrolledArgs(manager, actor, amount);
    }

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnScrolledArgs with(InputConsumer actor){return actor==this.actor?this:make(manager, actor, amount);}

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnScrolledArgs with(InputConsumer actor, int amount){return make(manager, actor, amount);}
}
