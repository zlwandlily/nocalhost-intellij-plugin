package dev.nocalhost.plugin.intellij.commands.data;

import dev.nocalhost.plugin.intellij.api.data.DevSpace;

public class NhctlListApplicationOptions extends NhctlGlobalOptions {
    public NhctlListApplicationOptions(String kubeConfig, String namespace) {
        super(kubeConfig, namespace);
    }
}
