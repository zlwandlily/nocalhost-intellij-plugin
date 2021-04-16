package dev.nocalhost.plugin.intellij.api.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ServiceAccount {
    @SerializedName("cluster_id")
    private int clusterId;
    @SerializedName("kubeconfig")
    private String kubeConfig;
    @SerializedName("storage_class")
    private String storageClass;
    private boolean privilege;
    @SerializedName("namespace_packs")
    private Namespace[] namespaces;

    @Data
    public static class Namespace {
        @SerializedName("space_id")
        private Integer spaceId;
        private String namespace;
        @SerializedName("spacename")
        private String spaceName;
    }
}
