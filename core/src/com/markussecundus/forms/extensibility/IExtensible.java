package com.markussecundus.forms.extensibility;

import java.util.HashMap;
import java.util.Map;


/**
 * Canonical implementation of {@link Extensible}, that provides extensions map
 * as a lazily created instance of {@link HashMap} (to minimize overhead in case no extension is ever used).
 *
 * @see Extensible
 *
 * @see com.markussecundus.forms.elements.impl.BasicAbstractDrawableElem
 *
 * @author MarkusSecundus
 * */
public class IExtensible implements Extensible {

    @Override
    public Map<ExtensionType<?>, Object> getExtensionsMap() {
        if(extensions == null)
            extensions = new HashMap<>();
        return extensions;
    }

    private Map<ExtensionType<?>,Object> extensions = null;
}
