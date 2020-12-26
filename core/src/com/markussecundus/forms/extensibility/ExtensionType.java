package com.markussecundus.forms.extensibility;

public interface ExtensionType<TExtension> {
    public TExtension createInstance(Extensible toBeExtended);
}
