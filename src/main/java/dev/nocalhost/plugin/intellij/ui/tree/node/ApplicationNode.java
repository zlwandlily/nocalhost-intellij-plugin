package dev.nocalhost.plugin.intellij.ui.tree.node;

import javax.swing.tree.DefaultMutableTreeNode;

import dev.nocalhost.plugin.intellij.api.data.Application;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationNode extends DefaultMutableTreeNode {
    private Application application;
    private boolean expanded;
    private boolean installed;

    public ApplicationNode(Application application) {
        this(application, false, false);
    }

    @Override
    public boolean isLeaf() {
        return !installed;
    }

    public ApplicationNode clone() {
        return new ApplicationNode(application, expanded, installed);
    }
}
