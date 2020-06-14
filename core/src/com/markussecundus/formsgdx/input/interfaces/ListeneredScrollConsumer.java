package com.markussecundus.formsgdx.input.interfaces;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.args.OnScrolledArgs;

/**
 * Rozhraní pro {@link InputConsumer}, jenž má pro zpracovávání událostí kolečka myši dedikovaný {@link EventDelegate}.
 *
 * Pro kanonickou implementaci viz {@link com.markussecundus.formsgdx.input.mixins.IListeneredScrollConsumer}.
 *
 * @see com.markussecundus.formsgdx.input.mixins.IListeneredScrollConsumer
 *
 * @see ListeneredKeyConsumer
 * @see ListeneredTouchConsumer
 *
 * @see ListeneredUniversalConsumer
 *
 * @author MarkusSecundus
 * */
public interface ListeneredScrollConsumer extends InputConsumer {

    /**
     * @return Delegát zpracovávající volání <code>scrolled</code>.
     * */
    ConstProperty<EventDelegate<OnScrolledArgs>> onScrollListener();


    /**
     * Vykoná <code>onScrollListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override
    default boolean scrolled(OnScrolledArgs e){
        return getOnScrollListener().exec(e.with(this));
    }

    /**
     * Pohodlnější zkratka pro <code>onScrollListener().get()</code>
     * */
    default EventDelegate<OnScrolledArgs> getOnScrollListener(){ return onScrollListener().get(); }

}
