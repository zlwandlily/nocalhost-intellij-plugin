package dev.nocalhost.plugin.intellij.ui.tree.node;

import javax.swing.tree.DefaultMutableTreeNode;

import dev.nocalhost.plugin.intellij.api.data.ServiceAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceAccountNode extends DefaultMutableTreeNode {
    private ServiceAccount serviceAccount;
    private boolean expanded;

    public ServiceAccountNode(ServiceAccount serviceAccount) {
        this(serviceAccount, false);
    }

    public ServiceAccountNode clone() {
        return new ServiceAccountNode(serviceAccount, expanded);
    }
}
