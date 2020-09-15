package com.markussecundus.formsgdx.input.interfaces;


import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.mixins.IListeneredUniversalConsumer;

/**
 * Rozhraní pro {@link InputConsumer}, jenž má pro zpracovávání všech vstupních událostí dedikované {@link EventDelegate}s.
 *
 * Pro kanonickou implementaci viz {@link IListeneredUniversalConsumer}.
 *
 * @see IListeneredUniversalConsumer
 *
 * @see com.markussecundus.formsgdx.input.interfaces.ListeneredKeyConsumer
 * @see ListeneredTouchConsumer
 * @see com.markussecundus.formsgdx.input.interfaces.ListeneredScrollConsumer
 *
 * @author MarkusSecundus
 * */
public interface ListeneredUniversalConsumer extends ListeneredScrollConsumer, ListeneredTouchConsumer, ListeneredKeyConsumer {}
