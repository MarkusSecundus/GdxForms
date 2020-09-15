package com.markussecundus.forms.wrappers.property.binding;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.events.ListenerPriorities;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * Listener zajišťující komunikaci zdrojové vazebné property s {@link BindingExecutor}em.
 * <p>Při zjištění nové hodnoty property zajistí, že změna bude rozdistribuována do vázaných properties.
 *
 * @see Binding
 *
 * @see Bindings
 * @see BindingExecutor
 *
 *
 * @author MarkusSecundus
 * */
public class BinderListener<T> implements EventListener<ReadonlyProperty.SetterListenerArgs<?>> {

    /**
     * Postaví novou instanci nad danými hodnotami.
     *
     * @param executor Instance {@link BindingExecutor}u, skrze kterou je daný binding proveden.
     * @param bindings Množina všech bindingů, jež se vztahují na otcovskou property.
     * */
    public BinderListener(BindingExecutor executor, List<Binding<T>> bindings){
        this.executor = executor;
        this.bindings = bindings;
    }

    /**
     * Postaví novou instanci nad danými hodnotami.
     * <p>
     * Pro seznam bindingů použije novou prázdnou kolekci.
     *
     * @param executor Instance {@link BindingExecutor}u, skrze kterou je daný binding proveden.
     * */
    public BinderListener(BindingExecutor executor){this(executor, new ArrayList<>());}

    /**
     * Instance {@link BindingExecutor}u, skrze kterou je daný binding proveden.
     * */
    public final BindingExecutor executor;

    /**
     * Množina všech bindingů, jež se vztahují na otcovskou property.
     * */
    public final Collection<Binding<T>> bindings;

    /**
     * Zařadí přiřazené bindingy do fronty a spustí průchod bindovacím grafem, pokud ještě neprobíhá.
     * */
    @Override
    public boolean exec(ReadonlyProperty.SetterListenerArgs<?> e) {
        executor.putActorValue((ReadonlyProperty)(e.caller()), e.newVal().get());
        for(Binding<T> bind: bindings)
            executor.enqueue(bind);
        executor.run();
        return true;
    }


    /**
     * Vrátí instanci přítomnou v daném delegátu. Pokud žádná neexistuje, vytvoří prázdnou a do delegáta ji přidá.
     * <p>
     * Hledá pouze mezi listenery s prioritou <code>{@link ListenerPriorities}.BINDING_EXECUTOR</code>.
     *
     * @param exec {@link BindingExecutor}, na něž má hledaný {@link BinderListener} být vázaný
     * @param delegate prohledávaný delegát
     *
     * @return instance {@link BinderListener}u příslušící danému {@link EventDelegate}.
     * */
    public static<T> BinderListener<T> findOrMakeIfNone(BindingExecutor exec, EventDelegate<? extends ReadonlyProperty.SetterListenerArgs<?>> delegate){
        int i = delegate.getListeners(ListenerPriorities.BINDING_EXECUTOR).indexOf(FormsUtil.finder(listener->(listener instanceof BinderListener<?>) && ((BinderListener<?>)listener).executor == exec));
        if(i == -1){
             BinderListener<T> ret = new BinderListener<>(exec);
             delegate.getListeners(ListenerPriorities.BINDING_EXECUTOR).add(ret);
            return ret;
        }
        else
            return (BinderListener<T>) delegate.getListeners(ListenerPriorities.BINDING_EXECUTOR).get(i);
    }
}