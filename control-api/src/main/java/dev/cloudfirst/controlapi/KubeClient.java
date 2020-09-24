package dev.cloudfirst.controlapi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

/**
 * KubeClient
 */
@ApplicationScoped
public class KubeClient {
    private ApiClient client;

    public KubeClient() throws IOException {
        String home = System.getProperty("user.home");
        String kubeConfigPath = home + "/.kube/config";

        try {
            client =  ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        } catch(FileNotFoundException ex) {
            client = ClientBuilder.cluster().build();
        }

        // set default config
        client.setVerifyingSsl(false);
        Configuration.setDefaultApiClient(client);
    }

    public ApiClient getApiClient() {
        return client;
    }
}