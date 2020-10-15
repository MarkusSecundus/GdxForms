package com.markussecundus.forms.wrappers.property.binding;

import com.markussecundus.forms.wrappers.ReadonlyWrapper;


/**
 * Objekt, který prochází bindovacím grafem a nastavuje hodnoty vázaných proměnných.
 * <p></p>
 * Požadavky, které musí splnovat:<p>
 *     - Během jednoho průchodu grafem bude každý vrchol navštíven pouze jednou, vrcholy, jež jsou ve frontě a již byly navštíveny,
 *       musí být podruhé přeskočeny. <p>
 *     - Hodnoty zdrojových properties budou přečteny pouze jednou za průchod, kešovány, a dále budou používány hodnoty uložené v keši,
 *       i když se reálná hodnota zdrojové property třeba změnila. Tím se zamezí zbytečnému volání getter-listenerů a příp. možnému nekonečnému zacyklení.<p>
 *     - Pořadí průchodu bindovacím grafem je libovolné v závislosti na implementaci.
 *       (Např. kanonická implementace umožnuje uživateli dodat libovolnou {@link com.markussecundus.forms.utils.datastruct.inout.InOutCollection} pro průchod grafem.)
 * </p>
 *
 *
 * @see IBindingExecutor
 *
 * @author MarkusSecundus
 *
 * */
public interface BindingExecutor {

    /**
     * Výchozí {@link BindingExecutor}, který bude k bindingu použit, pokud není specifikován jiný.
     * */
    public static final BindingExecutor DEFAULT = new Indirect(new IBindingExecutor());

    public static void setDefault(BindingExecutor newDefault){
        ((Indirect)DEFAULT).base = newDefault;
    }

    /**
     * Spustí průchod bindovacím grafem, pokud již aktuálně neprobíhá.
     * */
    public void run();

    /**
     * Nastaví pro aktuální průchod bindovacím grafem specifikovanou hodnotu jako hodnotu odpovídající dané property.
     *
     * @param actor {@link com.markussecundus.forms.wrappers.property.Property}, jejíž hodnota má být pro účely bindingu nastavena
     * @param value hodnota, která má při provádění bindingu být považována za hodnotu odpovídající dané property.
     * */
    public<T> void setValueForActor(ReadonlyWrapper<? super T> actor, T value);

    /**
     * Zařadí daný binding mezi čekající na zpracování.
     *
     * @param actor binding který je třeba zpracovat
     * */
    public void commitBinding(Binding<?> actor);





    
    public static class Indirect implements BindingExecutor{

        public Indirect(BindingExecutor base){this.base = base;}

        public BindingExecutor base;

        @Override
        public final void run() {
            base.run();
        }

        @Override
        public final <T> void setValueForActor(ReadonlyWrapper<? super T> actor, T value) {
            base.setValueForActor(actor, value);
        }

        @Override
        public final void commitBinding(Binding<?> actor) {
            base.commitBinding(actor);
        }
    }
}
