package com.markussecundus.formsgdx.input.args;

import com.badlogic.gdx.InputProcessor;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.InputManager;

/**
 * Datová třída reprezentující argument pro funkci <code>onMouseMoved</code> rozhraní {@link InputConsumer}.
 *
 * @see OnTouchDraggedArgs
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
public class OnMouseMovedArgs extends OnInputEventArgs{
    /**
     * Souřadnice, na kterých ke vstupní události došlo,
     * relativní vůči pozici objektu aktuálně vstup zpracovávajícího.
     * */
    public final int x,  y;

    /**
     * Inicializuje instanci danými hodnotami.
     * */
    protected OnMouseMovedArgs(InputManager manager, InputConsumer actor, int x, int y){
        super(manager, actor);
        this.x = x;
        this.y = y;
    }

    /**
     * @return Instance datové třídy sestávající z daných hodnot.
     * */
    public static OnMouseMovedArgs make(InputManager manager, InputConsumer actor, int x, int y){
        return new OnMouseMovedArgs(manager, actor, x, y);
    }

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnMouseMovedArgs with(InputConsumer actor){return actor==this.actor?this:make(manager, actor, x, y);}

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnMouseMovedArgs with(InputConsumer actor, int x, int y){return make(manager, actor, x, y);}


    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnTouchDraggedArgs with(InputConsumer actor, int x, int y, int pointer){return OnTouchDraggedArgs.make(manager, actor, x, y,pointer);}

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnTouchArgs with(InputConsumer actor, int x, int y, int pointer, int button){return OnTouchArgs.make(manager, actor, x, y,pointer, button);}
}
