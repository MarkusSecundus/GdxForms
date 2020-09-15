package com.markussecundus.forms.wrappers.property.binding;

import com.markussecundus.forms.utils.datastruct.DefaultDictByIdentity;
import com.markussecundus.forms.utils.datastruct.inout.InOutCollection;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;

import java.util.Map;


/**
 * Kanonická implementace {@link BindingExecutor}.
 *
 * @see Bindings
 *
 * @see Binding
 * @see BinderListener
 *
 * @see BindingExecutor
 *
 * @author MarkusSecundus
 * */
public class IBindingExecutor implements BindingExecutor{

    /**
     * Vytvoří novou instanci kolekce, jenž bude použita pro průchod grafem.
     * <p>
     * Volána pouze jednou, z konstruktoru.
     *
     * @return <code>new {@link InOutCollection}.Stack<>()</code>
     * */
    protected InOutCollection<Binding<?>> ACTORS_QUEUE__FACTORY(){return new InOutCollection.Stack<>(); }

    /**
     * Vytvoří novou instanci keše pro hodnoty již navštívených vrcholů bindovacího grafu.
     * <p>
     * Volána pouze jednou, z konstruktoru.
     *
     * @return <code>new {@link DefaultDictByIdentity}<>({@link ReadonlyWrapper}::get)</code>
     * */
    protected Map<ReadonlyWrapper<?>, Object> ALREADY_VISITED_ACTORS_VALUE_CACHE__FACTORY(){return new DefaultDictByIdentity<>(ReadonlyWrapper::get);}
    

    private final InOutCollection<Binding<?>> actors = ACTORS_QUEUE__FACTORY();

    private final Map<ReadonlyWrapper<?>, Object> alreadyVisitedActorsValueCache = ALREADY_VISITED_ACTORS_VALUE_CACHE__FACTORY();


    private volatile boolean isRunning = false;

    @Override
    public void run(){
        if(isRunning)
            return;

        try {
            isRunning = true;
            while (!actors.isEmpty()) {
                run_impl(actors.removeNext());
            }
        }finally {
            isRunning = false;
            alreadyVisitedActorsValueCache.clear();
            actors.clear();
        }
    }

    private void run_impl(Binding<?> next){
        if(alreadyVisitedActorsValueCache.containsKey(next.target))
            return;

        Object[] args = new Object[next.sources.length];

        for(int t=0;t<args.length;++t)
            args[t] = alreadyVisitedActorsValueCache.get(next.sources[t]);

        next.setValueOfTargetProperty(args);
    }


    @Override
    public<T> void putActorValue(ReadonlyWrapper<? super T> actor, T value){
        alreadyVisitedActorsValueCache.put(actor, value);
    }

    @Override
    public void enqueue(Binding<?> actor){
        if(alreadyVisitedActorsValueCache.containsKey(actor.target))
            return;
        this.actors.add(actor);
    }
}
