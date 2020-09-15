package com.markussecundus.forms.events;


/**
 * Statická třída specifikující některé konkrétní hodnoty priorit {@link EventListener}ů
 * používané napříč Formulářovou knihovnou.
 *
 * @see EventDelegate
 * @see EventListener
 *
 * @author MarkusSecundus
 * * */
public class ListenerPriorities {

    /**
     * Listener, který má kontrolovat zda hodnota splnuje nějaké kriterium a případně utnout běh delegáta hned na začátku.
     * */
    public static final int ARG_GUARD = 1000;

    /**
     * Listenery, který musí proběhnout před listenery uživatelskými.
     * */
    public static final int PRE_UTIL = 100;

    /**
     * Listenery přidané uživatelem.
     * */
    public static final int USER = 0;

    /**
     * Listenery, který musí proběhnout po listenerech uživatelských.
     * */
    public static final int POST_UTIL = -100;

    /**
     * Listener provádějící aktualizaci hodnot nabindovaných na property.
     * */
    public static final int BINDING_EXECUTOR = -1000;

    /**
     * Koncová rekurze :-D.
     * */
    public static final int TAIL_RECURSION = Integer.MIN_VALUE;

}
