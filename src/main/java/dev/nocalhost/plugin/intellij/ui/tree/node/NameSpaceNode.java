package dev.nocalhost.plugin.intellij.ui.tree.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NameSpaceNode extends DefaultResourceNode {

    private String namespace;
    private String spaceName;
    private String kubeConfig;
    private boolean expanded;

    public NameSpaceNode(String namespace, String spaceName, String kubeConfig) {
        this(namespace, kubeConfig, spaceName, false);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
