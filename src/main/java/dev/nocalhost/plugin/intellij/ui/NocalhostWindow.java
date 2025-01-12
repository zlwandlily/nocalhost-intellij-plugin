package dev.nocalhost.plugin.intellij.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.JBColor;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;

import dev.nocalhost.plugin.intellij.service.NocalhostBinService;
import dev.nocalhost.plugin.intellij.ui.action.AddStandaloneClustersAction;
import dev.nocalhost.plugin.intellij.ui.action.ConnectNocalhostApiServerAction;
import dev.nocalhost.plugin.intellij.ui.action.ManageNocalhostAccountsAction;
import dev.nocalhost.plugin.intellij.ui.action.RefreshAction;
import dev.nocalhost.plugin.intellij.ui.tree.NocalhostTree;

public class NocalhostWindow implements Disposable {
    private static final Logger LOG = Logger.getInstance(NocalhostWindow.class);

    private final Project project;

    private final SimpleToolWindowPanel panel;
    private NocalhostTree tree;

    public NocalhostWindow(Project project) {
        this.project = project;

        checkNocalhost();

        panel = new SimpleToolWindowPanel(true, false);

        init();
    }

    private void checkNocalhost() {
        NocalhostBinService nocalhostBinService = new NocalhostBinService(project);
        nocalhostBinService.checkBin();
        nocalhostBinService.checkVersion();
    }

    private void init() {
        tree = new NocalhostTree(project);
        new TreeSpeedSearch(tree);
        Disposer.register(this, tree);
        tree.updateDevSpaces();
        JBScrollPane scrollPane = new JBScrollPane(tree);
        scrollPane.setBorder(new TopLineBorder(new JBColor(0xD5D5D5, 0x323232), 1));
        panel.add(scrollPane);

        setToolbar();
    }

    private void setToolbar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AddStandaloneClustersAction(project));
        actionGroup.add(new Separator());
        actionGroup.add(new ConnectNocalhostApiServerAction(project));
        actionGroup.add(new ManageNocalhostAccountsAction(project));
        actionGroup.add(new Separator());
        actionGroup.add(new RefreshAction());

        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("Nocalhost.Toolbar", actionGroup, true);
        panel.setToolbar(actionToolbar.getComponent());
    }

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void dispose() {

    }
}
