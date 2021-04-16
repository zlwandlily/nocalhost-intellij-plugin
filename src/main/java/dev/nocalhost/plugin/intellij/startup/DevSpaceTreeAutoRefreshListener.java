package dev.nocalhost.plugin.intellij.startup;

import com.google.common.collect.Lists;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import dev.nocalhost.plugin.intellij.api.NocalhostApi;
import dev.nocalhost.plugin.intellij.api.data.Application;
import dev.nocalhost.plugin.intellij.api.data.ServiceAccount;
import dev.nocalhost.plugin.intellij.commands.NhctlCommand;
import dev.nocalhost.plugin.intellij.commands.data.NhctlListApplication;
import dev.nocalhost.plugin.intellij.commands.data.NhctlListApplicationOptions;
import dev.nocalhost.plugin.intellij.topic.NocalhostTreeDataUpdateNotifier;
import dev.nocalhost.plugin.intellij.topic.NocalhostTreeUiUpdateNotifier;
import dev.nocalhost.plugin.intellij.utils.KubeConfigUtil;

public class DevSpaceTreeAutoRefreshListener implements AppLifecycleListener {
    private static final Logger LOG = Logger.getInstance(DevSpaceTreeAutoRefreshListener.class);

    private static final long NOCALHOST_TREE_REFRESH_INTERVAL_MILLIS = 5000;

    private final AtomicBoolean nocalhostTreeDataUpdateMutex = new AtomicBoolean(false);

    private Thread updateThread = null;
    private boolean forceExit = false;

    @Override
    public void appStarted() {
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(
                NocalhostTreeDataUpdateNotifier.NOCALHOST_TREE_DATA_UPDATE_NOTIFIER_TOPIC,
                this::handleNocalhostTreeDataUpdate);

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            updateThread = Thread.currentThread();
            while (!forceExit) {
                try {
                    Thread.sleep(NOCALHOST_TREE_REFRESH_INTERVAL_MILLIS);
                } catch (InterruptedException ignored) {}
                ApplicationManager.getApplication().getMessageBus().syncPublisher(
                        NocalhostTreeDataUpdateNotifier.NOCALHOST_TREE_DATA_UPDATE_NOTIFIER_TOPIC
                ).action();
            }
        });
    }

    @Override
    public void appClosing() {
        forceExit = true;
        if (updateThread != null && !updateThread.isInterrupted()) {
            updateThread.interrupt();
        }
    }

    private void handleNocalhostTreeDataUpdate() {
        if (!nocalhostTreeDataUpdateMutex.compareAndSet(false, true)) {
            return;
        }
        try {
            final NocalhostApi nocalhostApi = ServiceManager.getService(NocalhostApi.class);
            final NhctlCommand nhctlCommand = ServiceManager.getService(NhctlCommand.class);

            List<ServiceAccount> serviceAccounts = nocalhostApi.listServiceAccount();
            List<Application> applications = nocalhostApi.listApplications();
            List<NhctlListApplication> nhctlListApplications = Lists.newArrayList();
            for (ServiceAccount serviceAccount : serviceAccounts) {
                if (serviceAccount.isPrivilege()) {
                    // TODO: get
                } else {
                    for (ServiceAccount.Namespace namespace : serviceAccount.getNamespaces()) {
                        String kubeConfig = KubeConfigUtil.kubeConfigPath(serviceAccount).toString();
                        nhctlListApplications.addAll(nhctlCommand.listApplication(new NhctlListApplicationOptions(kubeConfig, namespace.getNamespace())));
                    }
                }
            }
            ApplicationManager.getApplication().getMessageBus().syncPublisher(
                    NocalhostTreeUiUpdateNotifier.NOCALHOST_TREE_UI_UPDATE_NOTIFIER_TOPIC
            ).action(serviceAccounts, applications, nhctlListApplications);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            nocalhostTreeDataUpdateMutex.set(false);
        }
    }
}
