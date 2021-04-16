package dev.nocalhost.plugin.intellij.topic;

import com.intellij.util.messages.Topic;

import java.util.List;

import dev.nocalhost.plugin.intellij.api.data.Application;
import dev.nocalhost.plugin.intellij.api.data.ServiceAccount;
import dev.nocalhost.plugin.intellij.commands.data.NhctlListApplication;

public interface NocalhostTreeUiUpdateNotifier {
    @Topic.AppLevel
    Topic<NocalhostTreeUiUpdateNotifier> NOCALHOST_TREE_UI_UPDATE_NOTIFIER_TOPIC =
            new Topic<>(NocalhostTreeUiUpdateNotifier.class);

    void action(List<ServiceAccount> serviceAccounts,
                List<Application> applications,
                List<NhctlListApplication> nhctlListApplications);
}
