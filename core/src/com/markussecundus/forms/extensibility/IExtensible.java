package com.markussecundus.forms.extensibility;

import java.util.HashMap;
import java.util.Map;


/**
 * Kanonická implementace {@link Extensible}, jež v sobě obsahuje mapu rozšíření
 * jako líně inicializovanou instanci {@link HashMap}.
 *
 *
 * @see Extensible
 *
 * @see com.markussecundus.forms.elements.impl.BasicAbstractDrawableElem
 *
 * @author MarkusSecundus
 * */
public class IExtensible implements Extensible {

    @Override
    public Map<Class<?>, Object> getExtensionsMap() {
        if(extensions == null)
            extensions = new HashMap<>();
        return extensions;
    }

    private Map<Class<?>,Object> extensions = null;
}
