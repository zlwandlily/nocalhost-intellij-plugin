package dev.nocalhost.plugin.intellij.utils;

import org.apache.commons.codec.binary.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import dev.nocalhost.plugin.intellij.api.data.DevSpace;
import dev.nocalhost.plugin.intellij.api.data.ServiceAccount;

public class KubeConfigUtil {
    private static final Path KUBE_CONFIGS_DIR = Paths.get(
            System.getProperty("user.home"),
            ".nh/intellij-plugin/kubeConfigs");

    @Deprecated
    public static Path kubeConfigPath(DevSpace devSpace) {
        Path path = KUBE_CONFIGS_DIR.resolve(devSpace.getId() + "_" + devSpace.getNamespace() + "_config");
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.write(path, devSpace.getKubeConfig().getBytes(StandardCharsets.UTF_8));
            } else {
                String currentKubeConfig = new String(Files.readAllBytes(path));
                if (!StringUtils.equals(currentKubeConfig, devSpace.getKubeConfig())) {
                    Files.write(path, devSpace.getKubeConfig().getBytes(StandardCharsets.UTF_8));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    public static Path kubeConfigPath(ServiceAccount serviceAccount) {
        Path path = KUBE_CONFIGS_DIR.resolve(serviceAccount.getClusterId() + "_config");
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.write(path, serviceAccount.getKubeConfig().getBytes(StandardCharsets.UTF_8));
            } else {
                String currentKubeConfig = new String(Files.readAllBytes(path));
                if (!StringUtils.equals(currentKubeConfig, serviceAccount.getKubeConfig())) {
                    Files.write(path, serviceAccount.getKubeConfig().getBytes(StandardCharsets.UTF_8));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }
}
