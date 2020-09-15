package com.markussecundus.forms.utils.datastruct.redirected;


import com.markussecundus.forms.utils.function.Function;

import java.util.Collection;


/**
 * Wrapper nad {@link java.util.Collection}, jenž jí převádí na {@link Collection} nad jiným datovým typem.
 *
 * @param <From> typový parametr bazické kolekce
 * @param <To> typový parametr výsledné kolekce
 * */
public class RedirectedCollection<From, To> extends RedirectedCollectionGeneric<From, To, Collection<From>> {

    /**
     * Vytvoří přesměrovávanou kolekci nad danou bází a s danými převodními funkcemi.
     *
     * @param base Vnitřní implementace, k jejímuž přesměrování dojde.
     * @param convertor Funkce provádějící konverzi objektů bazického datového typu na objekty typu cílového.
     * @param backwardsConvertor Inverzní funkce k <code>convertor</code>u.
     * */
    public RedirectedCollection(Collection<From> base, Function<From, To> convertor, Function<To, From> backwardsConvertor) {
        super(base, convertor, backwardsConvertor);
    }
}