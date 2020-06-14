package com.markussecundus.formsgdx.input.interfaces;


import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.formsgdx.input.InputConsumer;

/**
 * Rozhraní pro {@link InputConsumer}, jenž má pro zpracovávání všech vstupních událostí dedikované {@link EventDelegate}s.
 *
 * Pro kanonickou implementaci viz {@link com.markussecundus.formsgdx.input.mixins.IListeneredUniversalConsumer}.
 *
 * @see com.markussecundus.formsgdx.input.mixins.IListeneredUniversalConsumer
 *
 * @see ListeneredKeyConsumer
 * @see ListeneredTouchConsumer
 * @see ListeneredScrollConsumer
 *
 * @author MarkusSecundus
 * */
public interface ListeneredUniversalConsumer extends ListeneredScrollConsumer, ListeneredTouchConsumer, ListeneredKeyConsumer {}
