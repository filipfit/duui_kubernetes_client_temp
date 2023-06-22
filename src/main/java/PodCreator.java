import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.util.Config;

import java.io.IOException;
import java.util.Collections;

public class PodCreator {
    public static void main(String[] args) throws IOException, ApiException  {
        removePod("test-pod-42");
    }

    static void removePod(String podName) throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();

        String namespace = "default"; // Replace with your desired namespace

        try {
            V1Pod deleteStatus = api.deleteNamespacedPod(podName, namespace, null, null, null, null, null, null);
            System.out.println("Pod deleted successfully. Status: " + deleteStatus.getStatus());
        } catch (ApiException e) {
            System.err.println("Failed to delete pod. Reason: " + e.getResponseBody());
        }

    }

    static void createPod(String name) throws IOException, ApiException {
        // Creates api client and configures it with cluster-specific auth credentials. ($HOME/.kube/config)
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        // CoreV1Api provides methods for Kubernetes interaction.
        CoreV1Api api = new CoreV1Api();

        // V1Pod object represents the pod to be created.
        V1Pod pod = new V1Pod();

        // Pod metadata
        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName(name);
        pod.setMetadata(metadata);

        // Set spec for pod
        V1Container container = new V1Container();
        container.setName("test-container-42");
        container.setImage("nginx:latest");
        pod.setSpec(new V1PodSpec().containers(Collections.singletonList(container)));

        // Create pod
        String namespace = "default";
        try {
            V1Pod createPod = api.createNamespacedPod(namespace, pod, null, null, null, null);
            System.out.println("Pod created successfully: " + createPod.getMetadata().getName());
        } catch (ApiException e) {
            System.err.println("Failed to create pod: " + e.getResponseBody());
        }
    }
}
