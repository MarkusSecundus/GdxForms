package com.markussecundus.forms.extensibility;

import com.markussecundus.forms.utils.datastruct.DefaultDictByIdentity;
import com.markussecundus.forms.utils.function.Function;

import java.util.HashMap;
import java.util.Map;


/**
 * Rozhraní, přes něž se k objektu připojují rozšiřující objekty - např. datové kontejnery pro mixiny implementované jako interface apod. .
 *
 * @see IExtensible
 *
 * @author MarkusSecundus
 * */
public interface Extensible {

    /**
     * @return tabulka rozšíření mapovaných na objekty jejich tříd
     * */
    public Map<Class<?>,Object> getExtensionsMap();

    /**
     * Zajistí, že rozšiřovaný objekt disponuje daným rozšířením (rozšíření příp. vytvoří)
     * a vrátí rozšiřující objekt.
     *
     * @param <T> typ hledaného rozšíření
     * @param cl třída na níž je mapovaný daný rozšiřující objekt
     * @param provider funkce, jež objekt rozšíření vytvoří, pokud není přítomen; jako argument bere právě rozšiřovaný objekt
     *
     * @return objekt rozšíření, pokud již je přítomen, příp. jeho nově vytvořená a k ostatním rozšířením přidaná instance
     * */
    public default<T> T getExtension(Class<? super T> cl, Function<Extensible, T> provider){
        Map<Class<?>, Object> extensions = getExtensionsMap();
        Object extension = extensions.get(cl);

        if(extension == null){
            extension = provider.apply(this);
            extensions.put(cl, extension);
        }
        return (T) extension;
    }




    /**
     * Primitivní implementace, jež sice jakožto <code>interface</code> nepotřebuje zakomponovat
     * do třídní hierarchie, avšak toho doshuje tak, že všechny mapy rozšíření k příslušným objektům
     * skladuje v globální tabulce, která nikdy za běhu programu není pročištěna a vede
     * tím pádem k memory-leaku (objekty, jež v ní jsou odkazované nikdy nebudou garbage-collectované,
     * ani když na ně není odkazováno z žádného jiného místa programu...).
     * <p></p>
     * Neměla by být používána, slouží spíše jako odstrašující příklad, poukazující na na první pohled
     * velmi lákavý pattern, jemuž je ale třeba se za každou cenu vyhnout.
     *
     * @see IExtensible
     * @see Extensible
     *
     * @author MarkusSecundus
     * */
    public static interface ImplementationThroughMemoryLeak extends Extensible{

        /**
         * {@inheritDoc}
         *
         * Najde, příp. vytvoří a najde rozšiřující {@link Map}u v globální tabulce indexované
         * aktuální instancí objektu.
         * */
        @Override
        default Map<Class<?>, Object> getExtensionsMap(){
            return Util.extensions.get(this);
        }

        /**
         * Třída obsahující privátní pole interfacu {@link ImplementationThroughMemoryLeak}.
         *
         * @see ImplementationThroughMemoryLeak
         *
         * @author MarkusSecundus
         * */
        public static class Util{
            private static final Map<ImplementationThroughMemoryLeak, Map<Class<?>, Object>> extensions = new DefaultDictByIdentity<>(self->new HashMap<>());
        }
    }
}
