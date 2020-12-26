package com.markussecundus.forms.extensibility;

import com.markussecundus.forms.utils.FormsUtil;

public abstract class IExtensionType<TExtension> implements ExtensionType<TExtension> {

    @Override
    public abstract TExtension createInstance(Extensible e);


    protected Object getID(){
        return this.getClass();
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof IExtensionType<?> && FormsUtil.equals(getID(), ((IExtensionType<?>) o).getID());
    }
}
