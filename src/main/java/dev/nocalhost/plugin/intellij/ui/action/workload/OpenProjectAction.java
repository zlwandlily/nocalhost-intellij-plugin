package dev.nocalhost.plugin.intellij.ui.action.workload;

import com.intellij.icons.AllIcons;
import com.intellij.ide.impl.OpenProjectTask;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.ex.ProjectManagerEx;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

import dev.nocalhost.plugin.intellij.ui.tree.node.ResourceNode;

public class OpenProjectAction extends DumbAwareAction {
    private final ResourceNode node;

    public OpenProjectAction(ResourceNode node) {
        super("Open Project", "", AllIcons.Actions.MenuOpen);
        this.node = node;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ProjectManagerEx.getInstanceEx().openProject(
                Paths.get(node.getNhctlDescribeService().getAssociate()),
                new OpenProjectTask());
    }
}
