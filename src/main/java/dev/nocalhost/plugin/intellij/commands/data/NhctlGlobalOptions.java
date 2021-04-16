package dev.nocalhost.plugin.intellij.commands.data;

import dev.nocalhost.plugin.intellij.api.data.DevSpace;
import dev.nocalhost.plugin.intellij.api.data.ServiceAccount;
import dev.nocalhost.plugin.intellij.utils.KubeConfigUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NhctlGlobalOptions {
    private boolean debug;
    private String kubeconfig;
    private String namespace;

    protected NhctlGlobalOptions(DevSpace devSpace) {
        kubeconfig = KubeConfigUtil.kubeConfigPath(devSpace).toString();
        namespace = devSpace.getNamespace();
    }

    protected NhctlGlobalOptions(String kubeConfig, String namespace) {
        this.kubeconfig = kubeConfig;
        this.namespace = namespace;
    }

    protected NhctlGlobalOptions(ServiceAccount serviceAccount, String namespace) {
        kubeconfig = KubeConfigUtil.kubeConfigPath(serviceAccount).toString();
        this.namespace = namespace;
    }
}
