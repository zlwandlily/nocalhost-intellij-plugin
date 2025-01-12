package dev.nocalhost.plugin.intellij.ui.console;

import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBPanelWithEmptyText;
import com.intellij.ui.paint.LinePainter2D;
import com.intellij.ui.switcher.QuickActionProvider;
import com.intellij.util.containers.JBIterable;
import com.intellij.util.ui.UIUtil;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

public class LogsToolWindowPanel extends JBPanelWithEmptyText implements QuickActionProvider, DataProvider {
    private JComponent myToolbar;
    private JComponent myContent;

    private final boolean myBorderless;
    protected boolean myVertical;
    private boolean myProvideQuickActions;

    public LogsToolWindowPanel(boolean vertical) {
        this(vertical, false);
    }

    public LogsToolWindowPanel(boolean vertical, boolean borderless) {
        setLayout(new BorderLayout(vertical ? 0 : 1, vertical ? 1 : 0));
        myBorderless = borderless;
        myVertical = vertical;
        setProvideQuickActions(true);

        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                Component child = e.getChild();

                if (child instanceof Container) {
                    ((Container) child).addContainerListener(this);
                }
                if (myBorderless) {
                    UIUtil.removeScrollBorder(LogsToolWindowPanel.this);
                }
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                Component child = e.getChild();

                if (child instanceof Container) {
                    ((Container) child).removeContainerListener(this);
                }
            }
        });
    }

    public boolean isVertical() {
        return myVertical;
    }

    public void setVertical(boolean vertical) {
        if (myVertical == vertical) return;
        removeAll();
        myVertical = vertical;
        setContent(myContent);
        setToolbar(myToolbar);
    }

    public boolean isToolbarVisible() {
        return myToolbar != null && myToolbar.isVisible();
    }

    public @Nullable JComponent getToolbar() {
        return myToolbar;
    }

    public void setToolbar(@Nullable JComponent c) {
        if (c == null) {
            remove(myToolbar);
        }
        myToolbar = c;
        if (myToolbar instanceof ActionToolbar) {
            ((ActionToolbar) myToolbar).setOrientation(myVertical ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL);
        }

        if (c != null) {
            if (myVertical) {
                add(c, BorderLayout.NORTH);
            } else {
                add(c, BorderLayout.EAST);
            }
        }

        revalidate();
        repaint();
    }

    @Override
    public @Nullable Object getData(@NotNull @NonNls String dataId) {
        return QuickActionProvider.KEY.is(dataId) && myProvideQuickActions ? this : null;
    }

    public LogsToolWindowPanel setProvideQuickActions(boolean provide) {
        myProvideQuickActions = provide;
        return this;
    }

    @Override
    public @NotNull java.util.List<AnAction> getActions(boolean originalProvider) {
        return collectActions(myToolbar);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    public @Nullable JComponent getContent() {
        return myContent;
    }

    public void setContent(@NotNull JComponent c) {
        if (myContent != null) {
            remove(myContent);
        }

        myContent = c;
        add(c, BorderLayout.CENTER);

        if (myBorderless) {
            UIUtil.removeScrollBorder(c);
        }

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (myToolbar != null && myToolbar.getParent() == this && myContent != null && myContent.getParent() == this) {
            g.setColor(JBColor.border());
            if (myVertical) {
                int y = (int) myToolbar.getBounds().getMaxY();
                LinePainter2D.paint((Graphics2D) g, 0, y, getWidth(), y);
            } else {
                int x = (int) myToolbar.getBounds().getMaxX();
                LinePainter2D.paint((Graphics2D) g, x, 0, x, getHeight());
            }
        }
    }

    @NotNull
    public static List<AnAction> collectActions(@Nullable JComponent component) {
        JBIterable<ActionToolbar> toolbars = UIUtil.uiTraverser(component).traverse().filter(ActionToolbar.class);
        if (toolbars.size() == 0) {
            return Collections.emptyList();
        }
        return toolbars.flatten(ActionToolbar::getActions).toList();
    }

}
